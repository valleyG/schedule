package com.yss.schedule.emue;

public enum TaskStatusEnum {
    /**
     *
     */
    UNGENERATE("UNGENERATE","A0000401"),
    UNAUDIT("UNAUDIT","A0000501")
    ;
    private String name;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    TaskStatusEnum(String name, String status) {
        this.name = name;
        this.status = status;
    }
}
