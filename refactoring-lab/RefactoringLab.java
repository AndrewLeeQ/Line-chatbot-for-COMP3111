import java.lang.StringBuilder;

/**
 * A representation of the result from a class.
 *
 * In this context, {@link #gradePoints()} refers to the grade points corresponding to the assigned
 * grade, e.g. a B corresponds to a grade point of 3.0.
 */
class ClassResult {
    private final String classCode;
    private final String className;
    private final double gradePoints;
    private final int credits;

    public ClassResult(
            String classCode, String className, double gradePoints, int credits) {
        this.classCode = classCode;
        this.className = className;
        this.gradePoints = gradePoints;
        this.credits = credits;
    }

    public String classCode() { return classCode; }
    public String className() { return className; }
    public double gradePoints() { return gradePoints; }
    public int credits() { return credits; }
}

/**
 * A representation of a transcript for a single semester.
 *
 * The {@link generateTranscriptForWidth} method prints the transcript to stdout. To determine the
 * number of lines the transcript will take before printing, {@link transcriptHeightForWidth} may
 * be used.
 */
class Transcript {
    private final String studentName;
    private final String studentId;
    private final String semesterName;
    private final ClassResult[] classResults;

    public Transcript(
            String studentName,
            String studentId,
            String semesterName,
            ClassResult[] classResults) {
        this.studentName = studentName;
        this.studentId = studentId;
        this.semesterName = semesterName;
        this.classResults = classResults;
    }

    public String studentName() { return studentName; }
    public String studentId() { return studentId; }
    public String semesterName() { return semesterName; }
    public ClassResult[] classResults() { return classResults; }

    /**
     * Prints the transcript to stdout.
     *
     * We design the transcript with the following format:
     * 1. First, the student name, ID, and semester name are printed on separate lines and centered
     *    relative to the provided width.
     * 2. Next, each class result is printed on a separate line. In the case that a line will
     *    exceed the width limit, the word is shifted to the next line with four-space indentation:
     *    
     *        COMP3111
     *            Software Engineering
     *            4.00 4
     *
     * 3. Finally, the semester GPA is printed.
     *
     * @param width The preferred width for each line, although this may not be respected if an
     *              individual field is too long.
     */
    
    void appendAlignCharacters(StringBuilder target, int width, String character) {
    	for(int i = 0; i < width; i++)
    		target.append(character);
    }
    
    void appendAlignedString(StringBuilder target, int width, String content, String alignCharacter) {
    	int spacesBefore = (width - content.length()) / 2;
    	int spacesAfter = (width - content.length() + 1) / 2;
        appendAlignCharacters(target, spacesBefore, alignCharacter);
        target.append(content);
        appendAlignCharacters(target, spacesAfter, alignCharacter);
        target.append("\n");    	
    }
    
    int appendStringToClassResult(StringBuilder target, String content, int width, int currentLength, int indentWidth, String indentString) {
    	if(currentLength + 1 + content.length() > width) {
    		target.append("\n");
    		for(int i = 0; i < indentWidth; i++)
    			target.append(indentString);
    		currentLength = 2;
    	}
    	else {
    		target.append(" ");
    		currentLength++;
    	}
    	target.append(content);
    	currentLength += content.length();
    	return currentLength;
    }
    
    void appendClassResult(StringBuilder target, ClassResult result, int width, int indentWidth, String indentString) {
    	int currentLength = result.classCode().length();
    	target.append(result.classCode());
    	
    	currentLength = appendStringToClassResult(target, result.className(), width, currentLength, indentWidth, indentString);
    	currentLength = appendStringToClassResult(target, String.format("%.2f", result.gradePoints()), width, currentLength, indentWidth, indentString);
    	currentLength = appendStringToClassResult(target, String.format("%d", result.credits()), width, currentLength, indentWidth, indentString);
    	
        target.append("\n");
    }
    
    public int generateTranscriptForWidth(int width, boolean output) {
        StringBuilder transcriptBuilder = new StringBuilder();
        
        String alignCharacter = "-";
        
        appendAlignedString(transcriptBuilder, width, studentName, alignCharacter);
        appendAlignedString(transcriptBuilder, width, studentId, alignCharacter);
        appendAlignedString(transcriptBuilder, width, semesterName, alignCharacter);
        
        // Newline between header and transcript body.
        transcriptBuilder.append("\n");
        
        double totalGradePoints = 0.0;
        int totalCredits = 0;
        for (ClassResult result : classResults) {
            // Accumulate the total grade points for later display.
            totalGradePoints += result.gradePoints() * result.credits();
            totalCredits += result.credits();
            
            appendClassResult(transcriptBuilder, result, width, 2, " ");
        }
        
        // Newline between the transcript and the summary.
        transcriptBuilder.append("\n");

        transcriptBuilder.append(
                String.format("Semester GPA: %.2f", totalGradePoints / totalCredits));
        
        String transcript = transcriptBuilder.toString();
        if(output)
        	System.out.println(transcript);
        int lineCount = 0;
        for(int i = 0; i < transcript.length(); i++) {
        	if(transcript.charAt(i) == '\n')
        		lineCount++;
        }
        return lineCount + 1;
    }

    /**
     * Returns the total number of lines required to display the transcript.
     *
     * For more information about the transcript format, see {@link generateTranscriptForWidth}.
     *
     * @param width The preferred width for each line.
     * @return The total number of lines required to display the transcript with respect to the
     *         {@code width} argument.
     */
    public int transcriptHeightForWidth(int width) {
    	return generateTranscriptForWidth(width, false);
    }
}

public class RefactoringLab {
    public static void main(String[] args) {
        ClassResult[] classResults = new ClassResult[] {
            new ClassResult("COMP3111", "Software Engineering", 4.0, 4),
            new ClassResult("COMP3311", "Database Management Systems", 3.3, 3),
        };
        Transcript transcript = new Transcript("John Chan", "21039408", "2017F", classResults);
        transcript.generateTranscriptForWidth(20, true);
        System.out.println("Total lines: " + transcript.transcriptHeightForWidth(20));
    }
}