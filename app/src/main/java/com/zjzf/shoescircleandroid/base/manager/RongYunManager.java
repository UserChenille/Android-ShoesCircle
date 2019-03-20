package com.zjzf.shoescircleandroid.base.manager;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Looper;
import android.view.View;

import com.zjzf.shoescircle.lib.helper.RxHelper;
import com.zjzf.shoescircle.lib.other.WeakHandler;
import com.zjzf.shoescircle.lib.utils.LogHelper;
import com.zjzf.shoescircle.lib.utils.StringUtil;
import com.zjzf.shoescircleandroid.base.im.IMReceiveObserver;
import com.zjzf.shoescircleandroid.base.net.apis.UserApis;
import com.zjzf.shoescircleandroid.base.net.client.HandleServerApiErrorAction;
import com.zjzf.shoescircleandroid.base.net.client.OnResponseListener;
import com.zjzf.shoescircleandroid.base.net.client.RetrofitClient;
import com.zjzf.shoescircleandroid.base.net.model.ObjectData;
import com.zjzf.shoescircleandroid.model.UserDetailInfo;
import com.zjzf.shoescircleandroid.model.UserInfo;
import com.zjzf.shoescircleandroid.ui.im.message.TransactionMessage;
import com.zjzf.shoescircleandroid.ui.im.message.TransactionMessageProvider;
import com.zjzf.shoescircleandroid.ui.im.plugin.CustomIMPluginModule;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * Created by 陈志远 on 2018/9/29.
 */
public enum RongYunManager {
    INSTANCE;

    private static final String TAG = "RongYunManager";
    private WeakHandler mWeakHandler = new WeakHandler(Looper.getMainLooper());
    private ArrayList<WeakReference<IMReceiveObserver>> mObservers = new ArrayList<>();

    boolean isConnected;

    public void init(Application app) {
        RongIM.init(app);
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new CustomIMPluginModule());
            }
        }
        RongIM.registerMessageType(TransactionMessage.class);
        RongIM.registerMessageTemplate(new TransactionMessageProvider());
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public io.rong.imlib.model.UserInfo getUserInfo(final String userid) {
                RetrofitClient.get()
                        .create(UserApis.class)
                        .getUserById(userid)
                        .doOnNext(new HandleServerApiErrorAction<>())
                        .compose(RxHelper.<ObjectData<UserDetailInfo>>io_main())
                        .subscribe(new OnResponseListener<ObjectData<UserDetailInfo>>() {
                            @Override
                            public void onNext(ObjectData<UserDetailInfo> result) {
                                if (result.getData() == null) return;
                                UserDetailInfo detailInfo = result.getData();
                                io.rong.imlib.model.UserInfo userInfo = new io.rong.imlib.model.UserInfo(userid, detailInfo.getMemName(), Uri.parse(detailInfo.getAvatar()));
                                RongIM.getInstance().refreshUserInfoCache(userInfo);
                            }
                        });
                return null;
            }
        }, true);
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        RongIM.setConversationClickListener(new RongIM.ConversationClickListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, io.rong.imlib.model.UserInfo userInfo, String s) {
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, io.rong.imlib.model.UserInfo userInfo, String s) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });
    }

    public void connect() {
        if (!UserInfoManager.INSTANCE.isLogin() || isConnected()) return;
        UserInfo userInfo = UserInfoManager.INSTANCE.getUserInfo();
        connectInternal(userInfo.getImToken());
        RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
        RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link #init(Application)} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */
    private void connectInternal(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                isConnected = false;
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                isConnected = true;
                LogHelper.trace(TAG, "连接融云成功  >>>  " + userid);
                regRongImReceiveListener();
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                isConnected = false;
            }
        });

    }

    private void regRongImReceiveListener() {
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(final Message message, final int i) {
                LogHelper.trace(TAG,"收到消息：\n"+message.toString());
                for (final WeakReference<IMReceiveObserver> observer : mObservers) {
                    if (observer.get() != null) {
                        mWeakHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                observer.get().onReceived(message, i);
                            }
                        });
                    }
                }
                return false;
            }
        });
    }

    public void registerReceivedObserver(IMReceiveObserver imReceiveObserver) {
        boolean hasRegised = false;
        for (WeakReference<IMReceiveObserver> observer : mObservers) {
            if (observer.get() == imReceiveObserver) {
                hasRegised = true;
                break;
            }
        }
        if (!hasRegised) {
            mObservers.add(new WeakReference<IMReceiveObserver>(imReceiveObserver));
        }
    }

    public void unRegisterReceivedObserver(IMReceiveObserver imReceiveObserver) {
        Iterator iterator = mObservers.iterator();
        while (iterator.hasNext()) {
            WeakReference<IMReceiveObserver> observerWeakReference = (WeakReference<IMReceiveObserver>) iterator.next();
            if (observerWeakReference.get() == imReceiveObserver) {
                iterator.remove();
            }
        }
    }

    public boolean isConnected() {
        return isConnected;
    }


    public void disConnect() {
        RongIM.getInstance().disconnect();
    }

    public void sendMessage(MessageBuilder builder) {
        if (!isConnected()) return;
        switch (builder.type) {
            case MessageBuilder.TYPE_ONLY_TEXT:
                sendTextFieldMessage(builder);
                break;
            case MessageBuilder.TYPE_IMAGE:
                sendPicMessage(builder);
                break;
            case MessageBuilder.TYPE_VOICE:
                sendVoiceMessage(builder);
                break;
            case MessageBuilder.TYPE_CUSTOM:
                sendCustomMessage(builder);
                break;
        }
    }


    private void sendTextFieldMessage(final MessageBuilder builder) {
        if (!isBuilderCorrected(builder)) return;

        String message = ((MessageBuilder.TextFieldBuilder) builder.mChildBuilder).textField;
        TextMessage myTextMessage = TextMessage.obtain(message);
        Message msg = Message.obtain(builder.targetId, builder.mConversationType, myTextMessage);

        /**
         * <p>发送消息。
         * 通过 {@link io.rong.imlib.IRongCallback.ISendMessageCallback}
         * 中的方法回调发送的消息状态及消息体。</p>
         *
         * @param message     将要发送的消息体。
         * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
         *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
         *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，默认已经指定。
         * @param pushData    push 附加信息。如果设置该字段，用户在收到 push 消息时，能通过 {@link io.rong.push.notification.PushNotificationMessage#getPushData()} 方法获取。
         * @param callback    发送消息的回调，参考 {@link io.rong.imlib.IRongCallback.ISendMessageCallback}。
         */
        RongIM.getInstance().sendMessage(msg, message, message, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                handleAttached(builder, message);
            }

            @Override
            public void onSuccess(Message message) {
                handleSuccess(builder, message);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                handleError(builder, message, errorCode);
            }
        });
    }

    private void sendPicMessage(final MessageBuilder builder) {
        if (!isBuilderCorrected(builder)) return;
        /**
         * <p>根据会话类型，发送图片消息。</p>
         *
         * @param type        会话类型。
         * @param targetId    目标 Id。根据不同的 conversationType，可能是用户 Id、群组 Id 或聊天室 Id。
         * @param content     消息内容，例如 {@link TextMessage}, {@link ImageMessage}。
         * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
         *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
         *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，默认已经指定。
         * @param pushData    push 附加信息。如果设置该字段，用户在收到 push 消息时，能通过 {@link io.rong.push.notification.PushNotificationMessage#getPushData()} 方法获取。
         * @param callback    发送消息的回调。
         */

        MessageBuilder.ImageBuilder imageBuilder = (MessageBuilder.ImageBuilder) builder.mChildBuilder;
        ImageMessage imageMessage = ImageMessage.obtain(Uri.fromFile(new File(imageBuilder.thumImagePath))
                , Uri.fromFile(new File(imageBuilder.imagePath))
                , imageBuilder.sendOrigin);
        final Message msg = Message.obtain(builder.targetId, builder.mConversationType, imageMessage);

        RongIM.getInstance().sendImageMessage(msg, null, null, new RongIMClient.SendImageMessageCallback() {
            @Override
            public void onAttached(Message message) {
                handleAttached(builder, message);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                handleError(builder, message, errorCode);
            }

            @Override
            public void onSuccess(Message message) {
                handleSuccess(builder, message);
            }

            @Override
            public void onProgress(Message message, int i) {
                handleProgress(builder, message, i);
            }
        });
    }

    private void sendVoiceMessage(final MessageBuilder builder) {
        if (!isBuilderCorrected(builder)) return;

        String voiceFilePath = ((MessageBuilder.VoiceBuilder) builder.mChildBuilder).voiceFilePath;
        int duration = ((MessageBuilder.VoiceBuilder) builder.mChildBuilder).duration;

        VoiceMessage vocMsg = VoiceMessage.obtain(Uri.fromFile(new File(voiceFilePath)), duration);
        Message msg = Message.obtain(builder.targetId, builder.mConversationType, vocMsg);

        RongIM.getInstance().sendMessage(msg, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                handleAttached(builder, message);
            }

            @Override
            public void onSuccess(Message message) {
                handleSuccess(builder, message);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                handleError(builder, message, errorCode);
            }
        });

    }

    private void sendCustomMessage(final MessageBuilder builder) {
        if (!isBuilderCorrected(builder)) return;
        MessageContent content = ((MessageBuilder.CustomBuilder) builder.mChildBuilder).mContent;
        Message msg = Message.obtain(builder.targetId, builder.mConversationType, content);
        String contentData = new String(content.encode());

        RongIM.getInstance().sendMessage(msg, contentData, contentData, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                handleAttached(builder, message);
            }

            @Override
            public void onSuccess(Message message) {
                handleSuccess(builder, message);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                handleError(builder, message, errorCode);
            }
        });
    }

    void handleAttached(MessageBuilder builder, Message msg) {
        if (builder.mListener != null) {
            builder.mListener.onAttachedToDB(msg);
        }
    }

    void handleSuccess(MessageBuilder builder, Message msg) {
        if (builder.mListener != null) {
            builder.mListener.onSuccess(msg);
        }
    }

    void handleError(MessageBuilder builder, Message msg, RongIMClient.ErrorCode errorCode) {
        if (builder.mListener != null) {
            builder.mListener.onError(msg, errorCode);
        }
    }

    void handleProgress(MessageBuilder builder, Message msg, int progress) {
        if (builder.mListener != null) {
            builder.mListener.onProgress(msg, progress);
        }
    }

    private boolean isBuilderCorrected(MessageBuilder builder) {
        return builder != null && builder.isCorrected();
    }


    public static final class MessageBuilder {

        private static abstract class BaseChildBuilder {
            abstract MessageBuilder build();

            abstract boolean isCorrect();
        }

        static final int TYPE_ONLY_TEXT = 1;
        static final int TYPE_IMAGE = 2;
        static final int TYPE_VOICE = 3;
        static final int TYPE_CUSTOM = 4;
        private int type = -1;

        private BaseChildBuilder mChildBuilder;

        SendMessageResultListener mListener;
        String targetId;

        Conversation.ConversationType mConversationType = Conversation.ConversationType.PRIVATE;

        private MessageBuilder() {

        }

        public static MessageBuilder create() {
            return new MessageBuilder();
        }

        public MessageBuilder listener(SendMessageResultListener listener) {
            mListener = listener;
            return this;
        }

        public MessageBuilder conversationType(Conversation.ConversationType conversationType) {
            mConversationType = conversationType;
            return this;
        }

        public MessageBuilder targetId(String targetId) {
            this.targetId = targetId;
            return this;
        }

        public TextFieldBuilder text() {
            mChildBuilder = new TextFieldBuilder(this);
            return (TextFieldBuilder) mChildBuilder;
        }

        public CustomBuilder custom() {
            mChildBuilder = new CustomBuilder(this);
            return (CustomBuilder) mChildBuilder;
        }

        public ImageBuilder image() {
            mChildBuilder = new ImageBuilder(this);
            return (ImageBuilder) mChildBuilder;
        }

        public VoiceBuilder voice() {
            mChildBuilder = new VoiceBuilder(this);
            return (VoiceBuilder) mChildBuilder;
        }

        MessageBuilder build() {
            return this;
        }

        public static class TextFieldBuilder extends BaseChildBuilder {
            MessageBuilder mBuilder;
            String textField;

            public TextFieldBuilder(MessageBuilder builder) {
                mBuilder = builder;
                mBuilder.type = TYPE_ONLY_TEXT;
            }

            public TextFieldBuilder textField(String textField) {
                this.textField = textField;
                return this;
            }

            @Override
            public MessageBuilder build() {
                return mBuilder.build();
            }

            @Override
            boolean isCorrect() {
                return StringUtil.noEmpty(textField);
            }
        }

        public static class ImageBuilder extends BaseChildBuilder {
            MessageBuilder mBuilder;
            String imagePath;
            String thumImagePath;
            boolean sendOrigin;

            public ImageBuilder(MessageBuilder builder) {
                mBuilder = builder;
                mBuilder.type = TYPE_IMAGE;
            }

            public ImageBuilder imagePath(String imagePath) {
                this.imagePath = imagePath;
                return this;
            }

            public ImageBuilder thumImagePath(String thumImagePath) {
                this.thumImagePath = thumImagePath;
                return this;
            }

            public ImageBuilder sendOrigin(boolean sendOrigin) {
                this.sendOrigin = sendOrigin;
                return this;
            }

            @Override
            public MessageBuilder build() {
                return mBuilder.build();
            }

            @Override
            boolean isCorrect() {
                return StringUtil.noEmpty(imagePath);
            }
        }

        public static class VoiceBuilder extends BaseChildBuilder {
            MessageBuilder mBuilder;
            String voiceFilePath;
            int duration = 30;

            public VoiceBuilder(MessageBuilder builder) {
                mBuilder = builder;
                mBuilder.type = TYPE_VOICE;
            }

            public VoiceBuilder voiceFilePath(String voiceFilePath) {
                this.voiceFilePath = voiceFilePath;
                return this;
            }

            @Override
            public MessageBuilder build() {
                return mBuilder.build();
            }

            public VoiceBuilder duration(int duration) {
                this.duration = duration;
                return this;
            }

            @Override
            boolean isCorrect() {
                return StringUtil.noEmpty(voiceFilePath);
            }
        }

        public static class CustomBuilder extends BaseChildBuilder {
            MessageBuilder mBuilder;
            MessageContent mContent;

            public CustomBuilder(MessageBuilder builder) {
                mBuilder = builder;
                mBuilder.type = TYPE_CUSTOM;
            }

            public CustomBuilder setContent(MessageContent messageContent) {
                mContent = messageContent;
                return this;
            }

            @Override
            public MessageBuilder build() {
                return mBuilder.build();
            }

            @Override
            boolean isCorrect() {
                return mContent != null;
            }
        }

        boolean isCorrected() {
            return StringUtil.noEmpty(targetId) && mChildBuilder.isCorrect();
        }
    }

    public void setCurUser(UserDetailInfo user) {
        if (user == null) return;
        RongIM.getInstance().setCurrentUserInfo(new io.rong.imlib.model.UserInfo(user.getId(), user.getMemName(), Uri.parse(user.getAvatar())));
    }

    public interface SendMessageResultListener {
        void onAttachedToDB(Message message);

        void onSuccess(Message message);

        void onError(Message message, RongIMClient.ErrorCode errorCode);

        void onProgress(Message message, int progress);

        abstract class SimpleMessageResultListener implements SendMessageResultListener {
            @Override
            public void onAttachedToDB(Message message) {

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

            }

            @Override
            public void onProgress(Message message, int progress) {

            }
        }
    }
}
