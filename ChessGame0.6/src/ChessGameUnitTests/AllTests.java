package ChessGameUnitTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({AboutUnitTests.class, BoardTest.class, PieceBishopTest.class, PieceKingTest.class,PieceKnightTest.class, PiecePawnTest.class, PieceRookTest.class, BoardTest.class, CapturedPiecesTest.class, NonVisualPieceTest.class, PacketTest.class, PlayerTest.class, SquareTest.class, VisualPieceTest.class})
public class AllTests {

}
