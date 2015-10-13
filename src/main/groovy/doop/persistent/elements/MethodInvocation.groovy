package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class MethodInvocation extends Symbol {

    String methodInvocationID;
    Method invokingMethod;

    public MethodInvocation() {}

    public MethodInvocation(Position position, String sourceFileName, String methodInvocationID, Method invokingMethod) {
        super(position, sourceFileName);
        this.methodInvocationID = methodInvocationID;
        this.invokingMethod = invokingMethod;
    }
}
