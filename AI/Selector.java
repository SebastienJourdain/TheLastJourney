package AI;

public class Selector extends Node {

    @Override
    public Status execute(Objects.Character character) {
        for (Node child : children) {
            Status result = child.execute(character);
            if (result != Status.FAIL) {
                return result; 
            }
        }
        return Status.FAIL; 
    }
}


