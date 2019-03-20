package com.zjzf.shoescircleandroid.ui.im.plugin;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * Created by 陈志远 on 2018/12/9.
 */
public class CustomIMPluginModule extends DefaultExtensionModule {

    private InitTransactionPlugin myPlugin = new InitTransactionPlugin();

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
        pluginModules.add(myPlugin);
        return pluginModules;
    }
}
