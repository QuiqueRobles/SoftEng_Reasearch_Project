package paquete;

import de.learnlib.algorithms.ttt.base.AbstractTTTLearner;
import de.learnlib.algorithms.ttt.base.BaseDTNode;
import de.learnlib.algorithms.ttt.base.TTTState;
import de.learnlib.algorithms.ttt.base.TTTTransition;
import de.learnlib.algorithms.ttt.tree.TTT;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.oracle.MembershipOracle.MealyMembershipOracle;
import de.learnlib.oracle.equivalence.MealyBFInclusionOracle;
import de.learnlib.oracle.equivalence.MealyTestSuiteEQOracle;
import de.learnlib.oracle.equivalence.RandomWalkEQOracle;
import de.learnlib.oracle.membership.SimulatorOracle.MealySimulatorOracle;
import net.automatalib.words.Word;
import net.automatalib.words.impl.SimpleAlphabet;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        // Define the input alphabet
        SimpleAlphabet<String> alphabet = new SimpleAlphabet<>();

        // Add symbols to the alphabet
        alphabet.add("a");
        alphabet.add("b");

        // Create a membership oracle using a simulator
        MembershipOracle.MealyMembershipOracle<String, String> membershipOracle = new MealySimulatorOracle<>(
                new MiAutomata(), alphabet);

        // Create an equivalence oracle using a random words strategy
        MealyTestSuiteEQOracle<String, String> eqOracle = new MealyTestSuiteEQOracle<>(
                new RandomWordsEQOracle<>(membershipOracle, 10, 100));

        // Create an instance of the L* algorithm
        ExtensibleLStarMealy<String, String> lstar = new ExtensibleLStarMealy<>(alphabet, membershipOracle);

        // Start learning
        lstar.startLearning();

        // Refine the model until it is equivalent
        while (!eqOracle.isCounterexample(lstar.getHypothesisModel(), alphabet)) {
            Word<String> counterexample = eqOracle.findCounterExample(lstar.getHypothesisModel(), alphabet);
            lstar.refineHypothesis(counterexample);
        }

        // Print the resulting model
        MealyMachine<?, String, ?, String> hypothesisModel = lstar.getHypothesisModel();
        System.out.println(hypothesisModel);
    }
}
