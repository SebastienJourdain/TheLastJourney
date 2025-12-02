package AI;

import java.util.function.Function;

public class Action extends Node {

    private final String name;
    private final Function<Objects.Character, Status> action;

    public Action(String name, Function<Objects.Character, Status> action) {
        this.name = name;
        this.action = action;
    }

    @Override
    public Status execute(Objects.Character character) {
        return action.apply(character);
    }

    @Override
    public String toString() {
        return "Action: " + name;
    }
}
