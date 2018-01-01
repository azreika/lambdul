import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AstCommandImport extends AstCommand {
    private String filename;

    public AstCommandImport(String filename) {
        super("import");
        this.filename = filename;
    }

    @Override
    public AstCommandImport evaluate(AstEnvironment env) throws EvaluationException {
        // Open the input file
        Scanner inputFile;
        try {
            inputFile = new Scanner(new File(filename));
        } catch (FileNotFoundException fnfe) {
            throw new EvaluationException("file '" + filename  + "' does not exist");
        }

        // Evaluate all lines in the file
        while(inputFile.hasNextLine()) {
            String nextLine = inputFile.nextLine();

            // Parse the line as a program
            Parser parser = new Parser(nextLine);
            AstProgram program;
            try {
                program = parser.parseProgram();
            } catch (ParseException pe) {
                // Parsing error - stop evaluating the lines in the file
                // TODO: should all environment changes be undone?
                throw new EvaluationException("parsing error in file '" + this.filename + "': " + pe.getMessage());
            }

            // Evaluate the program
            program.evaluate(env);
        }

        inputFile.close();
        return this;
    }

    @Override
    public AstCommandImport clone() {
        return new AstCommandImport(filename);
    }

    @Override
    public String toString() {
        return "@import " + "\"" + this.filename + "\"";
    }
}
