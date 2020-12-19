package agh.cs.po;

import java.io.File;

public class SpriteComponent extends Component{
    private String sourceFile;
    private String color;

    public SpriteComponent(Scene scene, String sourceFile)
    {
        super(scene);
        this.sourceFile = sourceFile;
        this.setName("Sprite");
    }

    public String getSourceFile()
    {
        return sourceFile;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
