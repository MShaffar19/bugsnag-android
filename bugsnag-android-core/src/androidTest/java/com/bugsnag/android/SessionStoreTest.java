package com.bugsnag.android;

import static com.bugsnag.android.SessionStore.SESSION_COMPARATOR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

@SmallTest
public class SessionStoreTest {

    private File storageDir;

    /**
     * Generates a session store with 0 files
     *
     */
    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        SessionStore sessionStore = new SessionStore(context, NoopLogger.INSTANCE, null);
        assertNotNull(sessionStore.storeDirectory);
        storageDir = new File(sessionStore.storeDirectory);
        FileUtils.clearFilesInDir(storageDir);
    }

    /**
     * Removes any sessions in the store created during testing
     *
     */
    @After
    public void tearDown() {
        FileUtils.clearFilesInDir(storageDir);
    }

    @Test
    public void testComparator() {
        final String first = "1504255147933d06e6168-1c10-4727-80d8-627a5111775b.json";
        final String second = "1505000000000ef070b5b-fc0f-411e-8630-429acc477982.json";
        final String startup = "150450000000053a27e4e-967c-4e5c-91be-2e86f2eb7cdc.json";

        // handle defaults
        assertEquals(0, SESSION_COMPARATOR.compare(null, null));
        assertEquals(-1, SESSION_COMPARATOR.compare(new File(""), null));
        assertEquals(1, SESSION_COMPARATOR.compare(null, new File("")));

        // same value should always be 0
        assertEquals(0, SESSION_COMPARATOR.compare(new File(first), new File(first)));
        assertEquals(0, SESSION_COMPARATOR.compare(new File(startup), new File(startup)));

        // first is before second
        assertTrue(SESSION_COMPARATOR.compare(new File(first), new File(second)) < 0);
        assertTrue(SESSION_COMPARATOR.compare(new File(second), new File(first)) > 0);

        // startup is handled correctly
        assertTrue(SESSION_COMPARATOR.compare(new File(first), new File(startup)) < 0);
        assertTrue(SESSION_COMPARATOR.compare(new File(second), new File(startup)) > 0);
    }
}
