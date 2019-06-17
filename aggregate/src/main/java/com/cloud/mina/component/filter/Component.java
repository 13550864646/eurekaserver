package com.cloud.mina.component.filter;

import com.cloud.mina.unit_a.sportpackage.PackageData;
import org.apache.mina.core.buffer.IoBuffer;

public interface Component {
    //添加子解码器
    public void add(Component t);

    //移除子解码器
    public void remove(Component t);

    //解析数据包
    public PackageData getDataFromBuffer(IoBuffer buffer);

    //从IOBuffer 中解析数据包
    public PackageData generateRealPackageData(IoBuffer buffer);
}
