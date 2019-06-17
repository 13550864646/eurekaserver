package com.cloud.mina.component.filter;

import com.cloud.mina.unit_a.sportpackage.PackageData;
import org.apache.mina.core.buffer.IoBuffer;

public class MHRootComponent extends PacketFilterComponent {

    public PackageData generateRealPackageData(IoBuffer buffer) {
        return null;
    }

    @Override
    public boolean check(IoBuffer buffer) {
        return false;
    }

}
