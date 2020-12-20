package agh.cs.projekt1;

import agh.cs.po.Controller;
import agh.cs.po.MapDirection;
import agh.cs.po.Pawn;
import agh.cs.po.Scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Genom extends Controller {
    protected static Random rand = new Random();
    protected int[] genes;
    protected int[] occurences;

    public Genom(Scene scene, int limiter)
    {
        this(scene, (ThreadLocalRandom.current().ints(0, 7)
                .limit(limiter)).toArray());
    }

    public Genom(Scene scene, int[] startGenes)
    {
        super(scene);
        this.setName("Genes");
        genes = startGenes;
        occurences = new int[8];
        for(int i = 0; i < genes.length; i++)
        {
            occurences[genes[i]]++;
        }
        FixGenes();
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
        //IntStream splitPoints = IntStream.concat(ThreadLocalRandom.current().ints(0, genes.length).
        //        distinct().limit(splitPointCount), Arrays.stream(new int[]{0,genes.length-1})).sorted();
        /*
        for(int i = 0; i < IntStream.concat(ThreadLocalRandom.current().ints(0, genes.length).
                distinct().limit(splitPointCount), Arrays.stream(new int[]{0,genes.length-1})).sorted().count(); i++)
        {
            if(thisCounter == 2)
            {
                newGenes = IntStream.concat(newGenes,
                        Arrays.stream(Arrays.copyOfRange(other.genes
                                ,IntStream.concat(ThreadLocalRandom.current().ints(0, genes.length).
                                        distinct().limit(splitPointCount), Arrays.stream(new int[]{0,genes.length-1})).sorted().toArray()[i],IntStream.concat(ThreadLocalRandom.current().ints(0, genes.length).
                                        distinct().limit(splitPointCount), Arrays.stream(new int[]{0,genes.length-1})).sorted().toArray()[i+1])));
            }
            else if(otherCounter == 2)
            {
                newGenes = IntStream.concat(newGenes,
                        Arrays.stream(Arrays.copyOfRange(this.genes
                                ,IntStream.concat(ThreadLocalRandom.current().ints(0, genes.length).
                                        distinct().limit(splitPointCount), Arrays.stream(new int[]{0,genes.length-1})).sorted().toArray()[i],IntStream.concat(ThreadLocalRandom.current().ints(0, genes.length).
                                        distinct().limit(splitPointCount), Arrays.stream(new int[]{0,genes.length-1})).sorted().toArray()[i+1])));
            }
            else if(i < 3)
            {
                System.out.println("i: " + i);
                boolean pickThis = rand.nextBoolean();
                newGenes = IntStream.concat(newGenes,
                        Arrays.stream(Arrays.copyOfRange((pickThis ? this.genes : other.genes)
                                ,IntStream.concat(ThreadLocalRandom.current().ints(0, genes.length).
                                        distinct().limit(splitPointCount), Arrays.stream(new int[]{0,genes.length-1})).sorted().toArray()[i],IntStream.concat(ThreadLocalRandom.current().ints(0, genes.length).
                                        distinct().limit(splitPointCount), Arrays.stream(new int[]{0,genes.length-1})).sorted().toArray()[i+1])));
                if(pickThis) thisCounter++;
                else otherCounter++;
            }
        }*/
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

        Genom newGenom = new Genom(scene, Arrays.stream(newGenes).sorted().toArray());
        newGenom.FixGenes();
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
