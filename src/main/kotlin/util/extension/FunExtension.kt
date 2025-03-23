package util.extension

/**
 * Combines two functions into a single function that executes both in sequence.
 *
 * This operator function allows you to use the `+` operator to combine two functions
 * that take no arguments and return no value. The resulting function, when invoked,
 * will execute the first function followed by the second function.
 *
 * @receiver The first function to be executed.
 * @param other The second function to be executed.
 * @return A new function that executes both the receiver and the `other` function in sequence.
 */
infix fun (() -> Unit).then(other: () -> Unit) =
    {
        this()
        other()
    }
