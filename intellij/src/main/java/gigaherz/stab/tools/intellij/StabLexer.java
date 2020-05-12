package gigaherz.stab.tools.intellij;

import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerPosition;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import stab.tools.helpers.CodeErrorManager;
import stab.tools.parser.IScanner;
import stab.tools.parser.LexicalUnit;
import stab.tools.parser.RestorePoint;
import stab.tools.parser.SourceCodeScanner;

public class StabLexer extends Lexer
{
    private IScanner lexer = new SourceCodeScanner(new ErrorManager(), new char[0]);
    private LexicalUnit currentLU;

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState)
    {
        lexer.setText(buffer);
        lexer.initialize(

        );
    }

    @Override
    public int getState()
    {
        return lexer.getNext();
    }

    @Override
    public @Nullable IElementType getTokenType()
    {
        return null;
    }

    @Override
    public int getTokenStart()
    {
        return lexer.getStartPosition();
    }

    @Override
    public int getTokenEnd()
    {
        return lexer.getEndPosition();
    }

    @Override
    public void advance()
    {
        currentLU = lexer.nextLexicalUnit();
    }

    @Override
    public @NotNull LexerPosition getCurrentPosition()
    {
        return new StabLexerPosition();
    }

    @Override
    public void restore(@NotNull LexerPosition position)
    {
        if (position instanceof StabLexerPosition)
        {
            lexer.restore(((StabLexerPosition)position).rp);
        }
    }

    @Override
    public @NotNull CharSequence getBufferSequence()
    {
        return null;
    }

    @Override
    public int getBufferEnd()
    {
        return 0;
    }

    private class ErrorManager extends CodeErrorManager
    {
    }

    private class StabLexerPosition implements LexerPosition
    {
        private final RestorePoint rp = lexer.createRestorePoint();

        @Override
        public int getOffset()
        {
            return rp.getStartPosition();
        }

        @Override
        public int getState()
        {
            return 0;
        }
    }
}
