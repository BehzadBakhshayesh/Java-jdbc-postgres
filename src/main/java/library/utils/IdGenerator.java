
package library.utils;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private static final AtomicLong bookCounter = new AtomicLong(1);
    private static final AtomicLong memberCounter = new AtomicLong(1);

    public static long generateBookId() {
        return bookCounter.getAndIncrement();
    }

    public static long generateMemberId() {
        return memberCounter.getAndIncrement();
    }

    public static void setBookCounter(long next) {
        bookCounter.accumulateAndGet(next, Math::max);
    }

    public static void setMemberCounter(long next) {
        memberCounter.accumulateAndGet(next, Math::max);
    }
}