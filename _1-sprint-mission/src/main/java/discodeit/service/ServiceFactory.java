package discodeit.service;

import discodeit.service.jcf.JCFChannelService;
import discodeit.service.jcf.JCFMessageService;
import discodeit.service.jcf.JCFUserService;

public class ServiceFactory {

    private static ServiceFactory instance;
    private final JCFUserService jcfUserService;
    private final JCFChannelService jcfchannelService;
    private final JCFMessageService jcfMessageService;

    private ServiceFactory() {
        jcfUserService = JCFUserService.getInstance();
        jcfchannelService = JCFChannelService.getInstance();
        jcfMessageService = JCFMessageService.getInstance();
    }

    public static ServiceFactory getInstance() {
        if(instance == null) {
            synchronized (ServiceFactory.class) {
                if(instance == null) {
                    instance = new ServiceFactory();
                }
            }
        }
        return instance;
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
