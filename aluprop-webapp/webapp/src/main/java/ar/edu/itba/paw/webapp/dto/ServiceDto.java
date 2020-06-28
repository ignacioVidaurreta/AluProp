package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Service;

public class ServiceDto {

    private ServiceDto() { }

    private long id;
    private String name;

    public static ServiceDto fromService(Service service) {
        ServiceDto ret = new ServiceDto();
        ret.id = service.getId();
        ret.name = service.getName();
        return ret;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
