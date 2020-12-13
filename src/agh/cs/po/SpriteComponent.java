package agh.cs.po;

import java.io.File;

public class SpriteComponent extends Component{
    private String sourceFile;

    public SpriteComponent(String sourceFile)
    {
        super();
        this.sourceFile = sourceFile;
    }

    public String getSourceFile()
    {
        return sourceFile;
    }

    public boolean ChangeSource(String path)
    {
        if (new File(path).isFile())
        {
            this.sourceFile = path;
            return true;
        }
        return false;
    }

    @Override
    public void Tick() {

    }

    @Override
    public void Destroy() {

    }
}
