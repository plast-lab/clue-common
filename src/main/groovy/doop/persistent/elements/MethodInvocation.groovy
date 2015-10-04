package doop.persistent.elements

/**
 * Created by anantoni on 2/10/2015.
 */
class MethodInvocation extends Symbol {
    private String methodInvocationID;
    private Method invokingMethod;

    public MethodInvocation() {}

    public MethodInvocation(Position position, String compilationUnit, String methodInvocationID, Method invokingMethod) {
        super(position, compilationUnit, null);
        this.methodInvocationID = methodInvocationID;
        this.invokingMethod = invokingMethod;
    }
}
