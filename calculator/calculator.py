class Calculator:
    """A basic calculator which evaluates expressions (as lists)

    evaluate - evaluates a lsit of #s and ops to a float
    print_supported_ops - prints a guide to supported operations
    """
    
    def __init__(self):
        self.vars = {}

    def print_supported_ops(self):
        """Prints supported operations with their symbols"""

        print('Currently can add (+), subtract(-), multiply (*),'
             + ' and divide (/), and group with parentheses ()')

    def has_var(self, var: str) -> bool:
        """Determies whether a variable has a saved value

        :param var: the variable to check
        :type var: str
        :returns: whether that variable has a value
        :rtype: bool
        """
        try:
            self.vars[var]
            return True
        except KeyError:
            return False
    
    def assign(self, assign: list):
        self.vars[assign[0]] = self.evaluate(assign[2:])

    def evaluate(self, exp: list) -> float:
        """Evaluates an expression in list form

        :param exp: the expression to evaluate - [1, '*', 7, '-', 43]
        :type exp: list
        :returns: the value of the expression
        :rtype: float
        """

        # order of operations!
        cur = self.replace_vars(exp)
        cur = self.handle_paren(cur)
        cur = self.handle_expo(cur)
        cur = self.handle_mul_div(cur)
        cur = self.handle_add_sub(cur)
        return float(cur[0])

    def replace_vars(self, exp: list) -> list:
        """Replaces all variables in an expression with their values

        :param exp: the expression to evaluate - [1, '*', 7, '-', 43]
        :type exp: list
        :returns: the list with no variables left
        :rtype: list
        """
        
        new = []
        for part in exp:
            if self.has_var(part):
                new.append(self.vars[part])
            else:
                new.append(part)
                
        return new
    
    def handle_paren(self, exp: list) -> list:
        """Evalutes expressions inside of parentheses

        For all high-level parens (), NOT nested, replace with value of inner

        :param exp: the expression to evaluate - [1, '*', 7, '-', 43]
        :type exp: list
        :returns: the list with all parentheses dealt with
        :rtype: list
        """
        
        new = []
        hanging_parens = 0
        open_index = -1
        for i in range(len(exp)):
            if exp[i] == '(':
                if (open_index == -1):
                    open_index = i
                hanging_parens += 1
            elif exp[i] == ')':
                hanging_parens -= 1
                if hanging_parens == 0:
                    new.append(self.evaluate(exp[open_index + 1:i]))
                    open_index = -1
            elif hanging_parens == 0:
                new.append(exp[i])
                
        return new

    def handle_expo(self, exp: list) -> list:
        """Exponentiates numbers around ^

        For all instances of x, '^', 'y, replaces with x^y

        :param exp: the expression to evaluate - [1, '*', 7, '-', 43]
        :type exp: list
        :returns: the list with all * and / dealt with
        :rtype: list
        """
        
        new = list(exp)
        # index in new
        i = 0
        while i < len(new) - 1:
            # collapse list and move index back if * or /
            if new[i] == '^':
                new[i - 1:i + 2] = [new[i - 1] ** new[i + 1]]
                i -= 1
            else:
                i += 1
                
        return new         

    def handle_mul_div(self, exp: list) -> list:
        """Multiplies/divides numbers around * or /

        For all instances of x, '* or /', 'y, replaces with xy or x/y

        :param exp: the expression to evaluate - [1, '*', 7, '-', 43]
        :type exp: list
        :returns: the list with all * and / dealt with
        :rtype: list
        """
        
        new = list(exp)
        # index in new
        i = 0
        while i < len(new) - 1:
            # collapse list and move index back if * or /
            if new[i] == '*':
                new[i - 1:i + 2] = [new[i - 1] * new[i + 1]]
                i -= 1
            elif new[i] == '/':
                new[i - 1:i + 2] = [new[i - 1] / new[i + 1]]
                i -= 1
            else:
                i += 1
                
        return new
                
    def handle_add_sub(self, exp: list) -> list:
        """Adds/subtracts numbers around + or -

        For all instances of x, '+ or -', 'y, replaces with x+y or x-y

        :param exp: the expression to evaluate - [1, '*', 7, '-', 43]
        :type exp: list
        :returns: the list with all + and - dealt with
        :rtype: list
        """
        
        new = list(exp)
        # index in new
        i = 0
        while i < len(new) - 1:
            # collapse list and move index back if + or -
            if new[i] == '+':
                new[i - 1:i + 2] = [new[i - 1] + new[i + 1]]
                i -= 1
            elif new[i] == '-':
                new[i - 1:i + 2] = [new[i - 1] - new[i + 1]]
                i -= 1
            else:
                i += 1
                
        return new
