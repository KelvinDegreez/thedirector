package org.kelvin.webapp.schedule;

import org.kelvin.webapp.director.DataValues;

public class LifeTask {

    private String name = "";
    private Type type = Type.UNKNOWN;
    private Double timeCommitment = 0.0;
    private DataValues.Priority priority = DataValues.Priority.LOW;
    private DataValues.Urgency urgency = DataValues.Urgency.MINIMAL;

    public enum Type{
        SLEEP,
        WORK,
        REST,
        SPIRIT,
        LIFE_GOAL,
        RELATIONSHIP,
        TASKS,
        UNKNOWN
    }

    public LifeTask(){

    }

    public LifeTask(String name, Type type, Double timeCommitment, DataValues.Priority priority, DataValues.Urgency urgency){
        this.name = name;
        this.type = type;
        this.timeCommitment = timeCommitment;
        this.priority = priority;
        this.urgency = urgency;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTimeCommitment() {
        return timeCommitment;
    }

    public void setTimeCommitment(Double timeCommitment) {
        this.timeCommitment = timeCommitment;
    }

    public DataValues.Priority getPriority() {
        return priority;
    }

    public void setPriority(DataValues.Priority priority) {
        this.priority = priority;
    }

    public DataValues.Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(DataValues.Urgency urgency) {
        this.urgency = urgency;
    }

    @Override
    public String toString() {
        return name+" - "+type.toString()+" - "+timeCommitment;
    }
}