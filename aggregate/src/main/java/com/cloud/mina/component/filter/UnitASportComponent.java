package com.cloud.mina.component.filter;

import com.cloud.mina.unit_a.sportpackage.PackageData;
import org.apache.mina.core.buffer.IoBuffer;

public class UnitASportComponent extends MHRootComponent {
    @Override
    public boolean check(IoBuffer buffer) {
        log.info(" byte[O] =" + buffer.get(0) + " byte[l]=" + buffer.get(1) +
                " byte[2]=" + buffer.get(2) + " byte[3]=" + buffer.get(3));
        log.info("byte[4]=" + buffer.get(4) + " byte[5] =" + buffer.get(5) +
                " byte[6]=" + buffer.get(6) + " byte[7]=" + buffer.get(7));
        log.info(" byte[8]=" + buffer.get(8) + " byte[9] =" + buffer.get(9));
        if ((buffer.get(0) == -89) && (buffer.get(1) == -72) && (buffer.get(2) == 0) && (buffer.get(3) == 1)){
            log.info("buffer . length=" + buffer.array().length);
            log.info(this.getClass().getSimpleName() + ".check()return true");
            return true;
         }
            log.info(this.getClass().getSimpleName() + ".check()return false ");
            return false;
        }

    @Override
    public PackageData generateRealPackageData(IoBuffer buffer) {
        return null;
    }
}
