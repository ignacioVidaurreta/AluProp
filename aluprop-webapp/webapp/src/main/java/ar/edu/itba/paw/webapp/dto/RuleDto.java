package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Rule;

public class RuleDto {

    private RuleDto() { }

    private long id;
    private String name;

    public static RuleDto fromRule(Rule rule) {
        RuleDto ret = new RuleDto();
        ret.id = rule.getId();
        ret.name = rule.getName();
        return ret;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
