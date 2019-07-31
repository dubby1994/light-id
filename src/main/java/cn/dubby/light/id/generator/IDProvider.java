package cn.dubby.light.id.generator;

public abstract class IDProvider {

    protected int id;

    public abstract long increaseByDataSource();

    long provide() {
        return (increaseByDataSource() << 10) | id;
    }

}
