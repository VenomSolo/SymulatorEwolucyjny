package agh.cs.projekt1;

import agh.cs.po.Controller;
import agh.cs.po.MapDirection;
import agh.cs.po.Pawn;
import agh.cs.po.Scene;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Genom extends Controller {
    protected static Random rand = new Random();
    protected int[] genes;
    protected int[] occurences;

    static Genom BuildGenom(Scene scene, int limiter)
    {
        SimulationScene castScene = (SimulationScene)scene;
        var existingGenoms = castScene.existingGenoms;
        int[] newGenes = Fix(ThreadLocalRandom.current().ints(0, 7)
                .limit(limiter).sorted().toArray());
        if(existingGenoms.containsKey(Arrays.toString(newGenes)))
        {
            return existingGenoms.get(Arrays.toString(newGenes));
        }
        else return new Genom(scene, newGenes);
    }

    static Genom BuildGenomFrom(Scene scene, int[] newGenes)
    {
        SimulationScene castScene = (SimulationScene)scene;
        var existingGenoms = castScene.existingGenoms;
        if(existingGenoms.containsKey(Arrays.toString(newGenes))) return existingGenoms.get(Arrays.toString(newGenes));
        else return new Genom(scene, newGenes);
    }

    public Genom(Scene scene, int limiter)
    {
        this(scene, (ThreadLocalRandom.current().ints(0, 7)
                .limit(limiter)).toArray());
    }

    private List<Integer> MakeList(int[] array)
    {
        List<Integer> ret = new ArrayList<>();
        for(int i : array)
        {
            ret.add(i);
        }
        return ret;
    }

    public Genom(Scene scene, int[] startGenes)
    {
        super(scene);
        scene.Register("Genom", this);
        this.setName("Genes");
        genes = startGenes;
        occurences = new int[8];
        for(int i = 0; i < genes.length; i++)
        {
            occurences[genes[i]]++;
        }
        FixGenes();
        SimulationScene castScene = (SimulationScene)scene;
        var existingGenoms = castScene.existingGenoms;
        if(!existingGenoms.containsKey(Arrays.toString(genes)))
        {
            existingGenoms.put(Arrays.toString(genes), this);
        }
        //System.out.println();
    }

    public int ChooseRandomGene()
    {
        int chosenGene = rand.nextInt(genes.length);
        //System.out.println(chosenGene);
        for(int i = 0, sum = 0; i < occurences.length; i++)
        {
            sum+=occurences[i];
            if(sum>=chosenGene) return i;
        }
        return 0;

    }

    public Genom CombineGenes(Genom other)
    {
        //IntStream newGenes = IntStream.builder().build();
        int splitPointCount = 3;
        int thisCounter = 0;
        int otherCounter = 0;
        int [] newGenes = new int[0];
        int[] splitPoints = IntStream.concat(ThreadLocalRandom.current().ints(1, genes.length).
                       distinct().limit(splitPointCount), Arrays.stream(new int[]{0,genes.length})).sorted().toArray();
        for(int i = 0; i < splitPoints.length-1; i++)
        {
            if(thisCounter == 2)
            {
               newGenes = IntStream.concat(Arrays.stream(newGenes), Arrays.stream(
                       Arrays.copyOfRange(other.genes, splitPoints[i], splitPoints[i+1]))).toArray();
            }
            else if(otherCounter == 2)
            {
                newGenes = IntStream.concat(Arrays.stream(newGenes), Arrays.stream(
                        Arrays.copyOfRange(this.genes, splitPoints[i], splitPoints[i+1]))).toArray();
            }
            else if(i < 3)
            {
                //System.out.println("i: " + i);
                boolean pickThis = rand.nextBoolean();
                newGenes = IntStream.concat(Arrays.stream(newGenes), Arrays.stream(
                        Arrays.copyOfRange(pickThis? this.genes : other.genes, splitPoints[i], splitPoints[i+1]))).toArray();
                if(pickThis) thisCounter++;
                else otherCounter++;
            }
        }

        Genom newGenom = Genom.BuildGenomFrom(scene, Arrays.stream(newGenes).sorted().toArray());
        return newGenom;
    }

    private void FixGenes()
    {
        boolean errorFixed = false;
        for(MapDirection dir : MapDirection.values())
        {
            if (!Arrays.stream(genes).distinct().anyMatch(i -> i== dir.ordinal()))
            {
                genes[rand.nextInt(genes.length)] = dir.ordinal();
                errorFixed = true;
            }
        }

        if (errorFixed) FixGenes();
        else genes = Arrays.stream(genes).sorted().toArray();
    }

    static private int[] Fix(int[] potentialGenes)
    {
        boolean errorFixed = false;
        for(MapDirection dir : MapDirection.values())
        {
            if (!Arrays.stream(potentialGenes).distinct().anyMatch(i -> i== dir.ordinal()))
            {
                potentialGenes[rand.nextInt(potentialGenes.length)] = dir.ordinal();
                errorFixed = true;
            }
        }

        if (errorFixed) potentialGenes = Fix(potentialGenes);
        else potentialGenes = Arrays.stream(potentialGenes).sorted().toArray();
        return potentialGenes;
    }


    public void Rotate(Animal animal)
    {
        animal.Rotate(ChooseRandomGene());
    }

    public void Move(Animal animal)
    {
        animal.Move(animal.getOrientation().toUnitVector());
    }


    @Override
    public void Action(Pawn controlledPawn) {
        Animal animal = (Animal) controlledPawn;
        Rotate(animal);
        Move(animal);
    }

    @Override
    protected void OnPossess(Pawn pawn) {

    }

    @Override
    protected void OnUnpossess(Pawn pawn) {

    }

    @Override
    public void Tick() {

    }

    @Override
    public void Destroy() {
        super.Destroy();
        //scene.Unregister("AnimalController", this);
    }
}
