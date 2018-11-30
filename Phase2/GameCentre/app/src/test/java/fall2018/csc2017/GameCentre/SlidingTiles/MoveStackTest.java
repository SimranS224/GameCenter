package fall2018.csc2017.GameCentre.SlidingTiles;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveStackTest {
    MoveStack moveStack;

    @Before
    public void setUp() {
        moveStack = new MoveStack();
    }

    @Test
    public void add() {
        setUp();
        assertEquals(0, moveStack.size());
        Integer[] c = {0, 1, 2, 3};
        moveStack.add(c);
        assertEquals(1, moveStack.size());
    }

    @Test
    public void remove() {
        setUp();
        assertEquals(0, moveStack.size());
        Integer[] c = {0, 1, 2, 3};
        moveStack.add(c);
        assertEquals(1, moveStack.size());
        assertSame(c, moveStack.remove());
        assertEquals(0, moveStack.size());
    }

    @Test
    public void canUndo() {
        setUp();
        assertFalse(moveStack.canUndo());
        Integer[] c = {0, 1, 2, 3};
        moveStack.add(c);
        assertTrue(moveStack.canUndo());
        MoveStack.setNumUndos(-1);
        assertTrue(moveStack.canUndo());
        MoveStack.setNumUndos(0);
        assertFalse(moveStack.canUndo());
    }

    @Test
    public void getUndos() {
        setUp();
        MoveStack.setNumUndos(3);
        assertEquals(3, moveStack.getUndos());
        moveStack.remove();
        assertEquals(2, moveStack.getUndos());
        MoveStack.setNumUndos(10);
        assertEquals(10, moveStack.getUndos());

    }
}