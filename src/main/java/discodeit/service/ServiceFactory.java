package discodeit.service;

import discodeit.repository.jcf.JCFChannelRepository;
import discodeit.repository.jcf.JCFMessageRepository;
import discodeit.repository.jcf.JCFUserRepository;
import discodeit.service.jcf.JCFChannelService;
import discodeit.service.jcf.JCFMessageService;
import discodeit.service.jcf.JCFUserService;

public class ServiceFactory {

    private final JCFUserService jcfUserService;
    private final JCFChannelService jcfchannelService;
    private final JCFMessageService jcfMessageService;


    private final JCFUserRepository jcfUserRepository;
    private final JCFChannelRepository jcfChannelRepository;
    private final JCFMessageRepository jcfMessageRepository;
    public ServiceFactory() {
        //JCF
        jcfUserRepository = new JCFUserRepository();
        jcfChannelRepository = new JCFChannelRepository();
        jcfMessageRepository = new JCFMessageRepository();

        jcfUserService = JCFUserService.getInstance();
        jcfchannelService = JCFChannelService.getInstance();
        jcfMessageService = JCFMessageService.getInstance();

        //의존성 주입
        jcfUserService.setService();
        jcfchannelService.setService();
        jcfMessageService.setService();
    }


    //Getter
    public JCFUserService getJcfUserService() {
        return jcfUserService.getInstance();
    }

    public JCFChannelService getJcfchannelService() {
        return jcfchannelService.getInstance();
    }

    public JCFMessageService getJcfMessageService() {
        return jcfMessageService.getInstance();
    }

}
