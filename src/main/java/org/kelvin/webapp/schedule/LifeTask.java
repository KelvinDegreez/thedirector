package org.kelvin.webapp.schedule;

import org.kelvin.webapp.director.DataValues;

import java.time.DayOfWeek;
import java.util.*;

public class LifeTask {

    private String name = "";
    private Type type = Type.UNKNOWN;
    private Double timeCommitment = 0.0;
    private DataValues.Priority priority = DataValues.Priority.LOW;
    private DataValues.Urgency urgency = DataValues.Urgency.MINIMAL;
    private Set<DayOfWeek> repeatingDays = new TreeSet<>();
    private RepeatType repeatType;
    private int repeatCount;

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

    public enum RepeatType{
        DAYS,
        WEEKS,
        MONTHS,
        YEARS
    }

    public LifeTask(){

    }

    public LifeTask(String name, Type type, Double timeCommitment, DataValues.Priority priority, DataValues.Urgency urgency){
        this.name = name;
        this.type = type;
        this.timeCommitment = timeCommitment;
        this.priority = priority;
        this.urgency = urgency;
        this.repeatType = RepeatType.DAYS;
        this.repeatCount = 1;
        this.repeatingDays.addAll(Arrays.asList(DayOfWeek.values()));
    }

    public LifeTask(String name, Type type, Double timeCommitment, DataValues.Priority priority, DataValues.Urgency urgency, int repeatCount){
        this.name = name;
        this.type = type;
        this.timeCommitment = timeCommitment;
        this.priority = priority;
        this.urgency = urgency;
        this.repeatType = RepeatType.DAYS;
        this.repeatCount = repeatCount;
        this.repeatingDays.addAll(Arrays.asList(DayOfWeek.values()));
    }

    public LifeTask(String name, Type type, Double timeCommitment, DataValues.Priority priority, DataValues.Urgency urgency, RepeatType repeatType, int repeatCount, Set<DayOfWeek> repeatingDays){
        if(repeatType == RepeatType.DAYS){
            throw new IllegalArgumentException("Use \"LifeTask(String name, Type type, Double timeCommitment, DataValues.Priority priority, DataValues.Urgency urgency, int repeatCount)\" instead for repeating Days");
        }
        this.name = name;
        this.type = type;
        this.timeCommitment = timeCommitment;
        this.priority = priority;
        this.urgency = urgency;
        this.repeatType = repeatType;
        this.repeatCount = repeatCount;
        this.repeatingDays.addAll(repeatingDays);
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

    public Set<DayOfWeek> getRepeatingDays() {
        return repeatingDays;
    }

    public void setRepeatingDays(Set<DayOfWeek> repeatingDays) {
        this.repeatingDays = repeatingDays;
    }

    public RepeatType getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(RepeatType repeatType) {
        this.repeatType = repeatType;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    @Override
    public String toString() {
        return "["+timeCommitment+"]["+type.toString()+"]["+priority+"]["+urgency+"]["+name+"]";
    }
}