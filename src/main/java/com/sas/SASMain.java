package com.sas;

// import com.sas.parser.CustomSasParserListener;
// import com.sas.parser.ExpressionWriter;
import com.sas.parser.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SasMain {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Please provide a SAS file path");
            System.exit(1);
        }

        Path fileName = Paths.get(args[0]);
        String sasCode = new String(Files.readAllBytes(fileName));

        // Create lexer and parser
        CharStream input = CharStreams.fromString(sasCode);
        SasLexer lexer = new SasLexer(input);
        ExpressionWriter writer = getExpressionWriter(lexer);

        // Get original filename and create output filename
        String origFilename = fileName.getFileName().toString();
        String outFilename = origFilename.substring(0, origFilename.lastIndexOf('.')) + ".py";
        Path outputPath = fileName.getParent().resolve(outFilename);

        // Write to file
        try {
            Files.writeString(outputPath, writer.toString());
            System.out.println("Successfully wrote output to: " + outputPath);
        } catch (IOException e) {
            System.err.println("Error writing to output file: " + e.getMessage());
        }
    }

    private static ExpressionWriter getExpressionWriter(SasLexer lexer) {
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SasParser parser = new SasParser(tokens);

        // Parse the input
        ParseTree tree = parser.program();

        // Walk the parse tree with our listener
        ParseTreeWalker walker = new ParseTreeWalker();
        ExpressionWriter writer = new ExpressionWriter();
        Map<String, List<String>> imports = new HashMap<>();
        CustomSasParserListener listener = new CustomSasParserListener(writer, imports);
        walker.walk(listener, tree);
        ExpressionWriter writer1 = new ExpressionWriter();
        for (Map.Entry<String, List<String>> entry : imports.entrySet()) {
            if (entry.getValue().isEmpty()) {
                writer1.append("import");
                writer1.appendSpace();
                writer1.append(entry.getKey());
            } else {
                writer1.append("from");
                writer1.appendSpace();
                writer1.append(entry.getKey());
                writer1.appendSpace();
                writer1.append("import");
                writer1.appendSpace();
                writer1.append(entry.getValue().get(0));
                for (int i = 1; i < entry.getValue().size(); i++) {
                    writer1.append(", ");
                    writer1.append(entry.getValue().get(i));
                }
            }
            writer1.newlineAndIndent();
        }
        writer1.newlineAndIndent();
        writer1.append(writer);
        return writer1;
    }
}
