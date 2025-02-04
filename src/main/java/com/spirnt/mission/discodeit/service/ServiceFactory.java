package com.spirnt.mission.discodeit.service;

public interface ServiceFactory {
    UserService getUserService();

    ChannelService getChannelService();

    MessageService getMessageService();
}
