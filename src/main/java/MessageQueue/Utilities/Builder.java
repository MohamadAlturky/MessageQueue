package MessageQueue.Utilities;

import MessageQueue.Exceptions.MissingBuildAttributeException;

/**
 * @description This is the contract of the Builder Design Pattern.
 * @version <b style="font-size:20px;">&#8734;</b>
 * @author <a href="https://mohamadalturky.github.io/TurkyResume/"><b style="font-size:20px;">Mohamad Alturky</b></a>
 * @param <T> the type of the built instance.
 */
public interface Builder<T> {
    /**
     * @return a new instance of this T class.
     * @throws MissingBuildAttributeException when there is a missing build attribute.
     */
    T build() throws MissingBuildAttributeException;
}
