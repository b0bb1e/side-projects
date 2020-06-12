import calculator

calc = calculator.Calculator()

def run():
    """Provides a command-line user interface for the Calculator"""
    
    print('Welcome to my command-line calculator! '
          + 'Enter E at any time to exit')
    calc.print_supported_ops()
    
    while True:
        line = input('Enter: ')
        # termination flag
        if line == 'E':
            break
        try:
            # and = means an assignment
            if '=' in line:
                assignment = assignment_form(line)
                calc.assign(assignment)
            else:
                # parses text into list-form expression
                expression = expression_form(line)
                print('=' + str(calc.evaluate(expression)))
        # print out errors, but don't crash
        except ValueError as e:
                print(e)

def expression_form(line: str) -> list:
    """Parses a line of text into a list-form expression

    :param line: a line of text with numbers and operators
    :type line: str
    :returns: a list of numbers and operators, with whitespace
        stripped and multiple operators ('+-') resolved
    :rtype: list
    :raises: InvalidExpressionError if an invalid expression (too many ops,
        ops at end, unsupported op) is passed
    """
    
    exp = []
    cur_num = 0
    negate = False
    # tracked since all ops after first in a group are not saved
    ops_in_a_row = 0
    # hanging paren = open paren without close paren
    hanging_parens = 0
    
    for char in line:
        
        # save last value in exp for easy access
        if len(exp) == 0:
            last = None
        else:
            last = exp[len(exp) - 1]
        if char.isdigit():
            # allows build-up of multidigit numbers
            cur_num = cur_num * 10 + int(char)
            ops_in_a_row = 0
            
        elif is_operator(char):
            ops_in_a_row += 1
            
            # if number exists, it ends
            if cur_num:
                # implied multiplication by parentheses
                if last == ')':
                    exp.append('*')
                if negate:
                    cur_num = -cur_num
                    negate = False
                exp.append(cur_num)
                last = cur_num
                cur_num = 0
            
            # resolve runs of operators
            if is_paren(char):
                if char == '(':
                    # implied multiplication by parentheses
                    if isinstance(last, int):
                        exp.append('*')
                    hanging_parens += 1
                    ops_in_row = 1
                else:
                    hanging_parens -= 1
                    # closing parentheses act like a regular number
                    ops_in_a_row = 0
                exp.append(char)
            elif len(exp) == 0 or (is_operator(last, paren=False)
                                   or last == '('):
                if ops_in_a_row > 2:
                    raise InvalidExpressionError('Too many operators in a row')
                # a '-' after an operator is a negative sign
                if char == '-':
                    negate = True
                # a + after an operator is ignored
                elif char != '+':
                    raise InvalidExpressionError('Too many operators in a row')
            # first operator in a row is always added on
            else:
                exp.append(char)
        elif calc.has_var(char):
            # if number exists, it ends
            if cur_num:
                if negate:
                    cur_num = -cur_num
                    negate = False
                exp.append(cur_num)
                # implied multiplication of variable
                exp.append('*')
                exp.append(char)
                last = cur_num
                cur_num = 0
            else:
                # implied multiplication of varialbe
                if negate:
                    exp.append(-1)
                    exp.append('*')
                if calc.has_var(last):
                    exp.append('*')
                exp.append(char)
        # spaces are ignored
        elif not char.isspace():
            raise InvalidExpressionError('Unsupported operation')

    if len(exp) > 0:
        last = exp[len(exp) - 1]
    else:
        last = None
    # handle a number on at the end (is_operator elif won't trigger)
    if cur_num:
        # implied multiplication by parentheses
        if len(exp) > 0 and last == ')':
            exp.append('*')
        if negate:
            cur_num = -cur_num
        exp.append(cur_num)
    elif not (is_paren(last) or calc.has_var(last)):
        raise InvalidExpressionError('Cannot end with operators')
    if hanging_parens < 0:
        raise InvalidExpressionError('Cannot have more closing than '
                                     + 'opening parentheses')
    # close off all hanging parentheses
    for i in range(hanging_parens):
        exp.append(')')
        
    return exp

def assignment_form(line: str) -> list:
    assign = []
    for i in range(len(line)):
        if not assign and line[i].isalpha():
            assign.append(line[i])
        elif line[i] == '=':
            if '=' in line[i + 1:]:
                raise InvalidAssignmentError('Only 1 "=" sign allowed')
            try:
                assign.append('=')
                assign += expression_form(line[i + 1:])
            except InvalidExpressionError as e:
                raise InvalidAssignmentError(e)
            break
        
    return assign
            

def is_operator(char: str, paren: bool = True) -> bool:
    """Determines if a character is an operator (-+*/^)

    :param char: the char to check
    :type char: str (meant to be one character)
    :param paren: if parentheses are allowed
    :type paren: bool
    :returns: whether char matches one of -+*/^
    :rtype: bool
    """
    
    return (char == '-' or char == '+' or char == '*' or char == '/'
            or char == '^' or (paren and is_paren(char)))

def is_paren(char: str) -> bool:
    """Determines if a character is a parentheses ()

    :param char: the char to check
    :type char: str (meant to be one character)
    :returns: whether char matches ()
    :rtype: bool
    """
    
    return char == '(' or char == ')'

class InvalidExpressionError(ValueError):
    """Indicates that in parsing an expression was found invalid"""
    pass

class InvalidAssignmentError(ValueError):
    """Indicates that in parsing an assignment was found invalid"""
    pass

if __name__ == '__main__':
    run()
