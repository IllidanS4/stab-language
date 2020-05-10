package gigaherz.stab.tools.intellij;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class StabFileType extends LanguageFileType
{
    protected StabFileType()
    {
        super(StabLanguage.INSTANCE);
    }

    @Override
    public @org.jetbrains.annotations.NotNull String getName()
    {
        return "StabSource";
    }

    @Override
    public @org.jetbrains.annotations.NotNull
    @org.jetbrains.annotations.Nls(capitalization = Nls.Capitalization.Sentence) String getDescription()
    {
        return "Stab language source code";
    }

    @Override
    public @org.jetbrains.annotations.NotNull String getDefaultExtension()
    {
        return "stab";
    }

    @Override
    public @org.jetbrains.annotations.Nullable Icon getIcon()
    {
        return null;
    }
}
