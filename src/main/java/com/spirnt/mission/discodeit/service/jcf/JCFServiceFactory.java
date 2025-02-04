package com.spirnt.mission.discodeit.service.jcf;

import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.ServiceFactory;
import com.spirnt.mission.discodeit.service.UserService;

// JCFServiceFactory
public class JCFServiceFactory implements ServiceFactory {

    private final JCFUserService jcfUserService;
    private final JCFChannelService jcfchannelService;
    private final JCFMessageService jcfMessageService;


    public JCFServiceFactory() {
        jcfUserService = JCFUserService.getInstance();
        jcfchannelService = JCFChannelService.getInstance();
        jcfMessageService = JCFMessageService.getInstance();

        //의존성 주입
        jcfUserService.setService();
        jcfchannelService.setService();
        jcfMessageService.setService();
    }


    //Getter
    @Override
    public UserService getUserService() {
        return jcfUserService.getInstance();
    }

    @Override
    public ChannelService getChannelService() {
        return jcfchannelService.getInstance();
    }

    @Override
    public MessageService getMessageService() {
        return jcfMessageService.getInstance();
    }

}
