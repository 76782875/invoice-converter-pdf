package si.recek.invoiceConverter.synchronization;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Slavko on 08.02.2016.
 */
public class SalonUtils {

    public static <E> Collection<E> makeCollection(Iterable<E> iter) {
        Collection<E> list = new ArrayList<E>();
        for (E item : iter) {
            list.add(item);
        }
        return list;
    }
}
