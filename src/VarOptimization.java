import java.util.ArrayList;
import java.util.Arrays;

public class VarOptimization {
    private ArrayList<String> codeLines;
    private int num_vars;

    VarOptimization(String code, int num_vars, int total_vars) {
        this.num_vars = num_vars;

        this.codeLines = new ArrayList<String>(Arrays.asList(code.split("\n")));
        
        int var_removed = 0;
        String load = "iload";
        for (int var = (total_vars - num_vars); var <= this.num_vars; var++) {
            if (containsArray()) {
                return;
            }
        }
    

        for (int var = (total_vars - num_vars - var_removed); var <= this.num_vars; var++) {
            if (isUsed("iinc ", var)) 
                continue;
            
            if (var < 4) 
                load = "iload_";
            
            if(!isUsed(load, var)) {
                removeVar(var);
                updateVars(var);
            }
            else if (isConst(var))
                updateVars(var);
        } 
    }

    /** Variable is used */
    private boolean isUsed(String code, int var) {

        if (newSearchCode(code + var, false) != -1) 
            return true;

        return false;
    }

    /** Skip code with arrays */
    private boolean containsArray() {

        for (int i = 0; i < codeLines.size(); i++) {
            String line = codeLines.get(i).trim();
            if (line.contains("iastore")) 
                return true;
        }
        return false;
    }

    /** Removes variable  */
    private void removeVar(int var) {
        String store = "istore " + var;

        if (var < 4) 
            store = "istore_" + var;
        
        int index = newSearchCode(store, false);

        if (index != -1) {

            codeLines.remove(index); 
            codeLines.remove(index-1);
            
            this.num_vars--;
        }
    }

    /** Variable is const */
    private boolean isConst(int var) {
        String load = "iload ";
        String store = "istore ";

        if (var < 4) {
            store = "istore_";
            load = "iload_";
        }
        
        int index = newSearchCode(store+var, true); // skip first istore
        
        // var is const - no more than one store is done
        if (index == -1) {
            int index2 = newSearchCode(store+var, false); // gets the first store

            int load_index = newSearchCode(load+var, false);
            
            String value = newGetValue(store+var);

            if (!value.equals("")) {
                
                int num = Integer.parseInt(value);               
                
                replaceLine(load_index, num);

                codeLines.remove(index2-1);
                codeLines.remove(index2-1);

                this.num_vars--;

                return true;
            }
        }
        return false;
    }

    /** Updates variable indexes */
    private void updateVars(int deleted_var) {
        for (int i = deleted_var; i <= this.num_vars; i++) {

            for (int j = 0; j < codeLines.size(); j++) {
                
                if (codeLines.get(j).trim().equals("istore_" + (i+1))) {
                    codeLines.set(j, "\tistore_" + i);
                }
                if (codeLines.get(j).trim().equals("iload_" + (i+1))) {
                    codeLines.set(j, "\tiload_" + i);
                }
                if (codeLines.get(j).trim().equals("istore " + (i+1))) {
                    if (i < 4)
                        codeLines.set(j, "\tistore_" + i);
                    else 
                        codeLines.set(j, "\tistore " + i);
                }
                if (codeLines.get(j).trim().equals("iload " + (i+1))) {
                    if (i < 4)
                        codeLines.set(j, "\tiload_" + i);
                    else
                        codeLines.set(j, "\tiload " + i);
                }
                if (codeLines.get(j).trim().contains("iinc " + (i+1))) {
                    String lines[] = codeLines.get(j).split(" ");

                    codeLines.set(j, "\tiinc " + i + " " + lines[2]);
                }
            }
        }
    }

    /** Replace load from var with a const instruction */
    private void replaceLine(int index, int num) {
        if ((num >= 0) && (num <= 5)) {
            codeLines.set(index, "\ticonst_" + num);
        } 
        else if (num == -1) {
            codeLines.set(index,"\ticonst_m1");
        } 
        else if (num >= -128 && num <= 127) {
            codeLines.set(index,"\tbipush " + num);
        } 
        else if (num >= -32768 && num <= 32767) {
            codeLines.set(index,"\tsipush " + num);
        } 
        else {
            codeLines.set(index,"\tldc " + num);
        }
    }

    /** Search through code for a specific instruction */
    private int newSearchCode(String inst, boolean skipFirstStore) {
        String line = "";

        for (int i = 0; i < codeLines.size(); i++) {
            line = codeLines.get(i).trim();

            if (inst.contains("iinc")) {
                if (line.contains(inst))
                    return i;
            }

            if (line.equals(inst)) {
                if (inst.contains("istore") & skipFirstStore) {
                    skipFirstStore = false;
                    continue;
                }
                return i;
            }
        }
        return -1;
    }

    /** Get variable value */
    private String newGetValue(String inst) {
        String line = "";
        String previous_line = "";

        for (int i = 0; i < codeLines.size(); i++) {
            line = codeLines.get(i).trim();

            if (line.equals(inst)) {
                String value = previous_line.trim().replaceAll("\\D+","");

                return value;
            }
        
            previous_line = line;
        }

        return "";
    }

    /** Returns the code optimized */
    public String getOptimization() {
        String code = "";
        
        for (String line : codeLines) {
            code += line + '\n';
        }
        
        return code;
    }
    
    /** Returns the new number of arguments */
    public int getNewVars() {
        return this.num_vars;
    }

    /** For debug purposes - prints code on console */
    private void print() {
        System.out.println("\n ; Optimization\n");

        String code = "";

        for (String line : codeLines) {
            code += line + '\n';
        }

        System.out.println(code);


        System.out.println("\n ; End opt\n");
    }    
    
}