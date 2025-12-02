package AI;

public class Sequence extends Node {

    @Override
    public Status execute(Objects.Character character) {
        for (Node child : children) {
            Status result = child.execute(character);
            if (result != Status.SUCCESS) {
                return result; 
            }
        }
        return Status.SUCCESS; 
    }
}
