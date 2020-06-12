import runner
import calculator
import unittest

"""Unit tester for the runner and calculator"""

class KnownValues(unittest.TestCase):
    lines_to_expressions = (('12+6', [12, '+', 6]),
                            ('12    + 6', [12, '+', 6]),
                            ('12++6', [12, '+', 6]),
                            ('12+-6', [12, '+', -6]),
                            ('12 +    -   6', [12, '+', -6]),
                            ('12-+6', [12, '-', 6]),
                            ('12*-6', [12, '*', -6]),
                            ('12/-6', [12, '/', -6]),
                            ('-12*6', [-12, '*', 6]),
                            ('+12*-6', [12, '*', -6]),
                            ('12', [12]),
                            ('-6', [-6]),
                            ('12-(5*4)', [12, '-', '(', 5, '*', 4, ')']),
                            ('12-5(4)', [12, '-', 5, '*', '(', 4, ')']),
                            ('12+-5(+4)', [12, '+', -5, '*', '(', 4, ')']),
                            ('(5+3)/-2', ['(', 5, '+', 3, ')', '/', -2]),
                            ('(5+3)--2', ['(', 5, '+', 3, ')', '-', -2]),
                            ('(5+3)-+2', ['(', 5, '+', 3, ')', '-', 2]),
                            ('((4/-3)+3)-12', ['(', '(', 4, '/', -3, ')',
                                               '+', 3, ')', '-', 12]),
                            ('(3-(6 * 4', ['(', 3, '-', '(', 6, '*', 4,
                                           ')', ')']),
                            ('3-(4)6', [3, '-', '(', 4, ')', '*', 6]),
                            ('3--1(4)', [3, '-', -1, '*', '(', 4, ')']),
                            ('(3+7)/(4-2)', ['(', 3, '+', 7, ')', '/',
                               '(', 4, '-', 2, ')']))

    invalid_lines = ('12+++6', '12+/6', '12*/6', '12+*6', '12-/6', '12//6',
                     '12/++6', '12*-+6', '12+6/', '12-6--', '*+12', '/3',
                     '12^6', '12--(6))', '6)')
    
    expressions_to_values = (([12, '+', 6], 18),
                             ([12, '+', -6], 6),
                             ([12, '-', 6], 6),
                             ([12, '*', -6], -72),
                             ([12, '/', -6], -2),
                             ([-12, '*', 6], -72),
                             ([12], 12),
                             ([-6], -6),
                             ([12, '+', -6, '/', 5], 10.8),
                             ([12, '-', '(', 5, '*', 4, ')'], -8),
                             ([12, '-', 5, '*', '(', 4, ')'], -8),
                             ([12, '+', -5, '*', '(', 4, ')'], -8),
                             (['(', 5, '+', 3, ')', '/', -2], -4),
                             (['(', 5, '+', 3, ')', '-', -2], 10),
                             (['(', 5, '+', 3, ')', '-', 2], 6),
                             (['(', '(', 4, '/', -5, ')',
                                   '+', 3, ')', '-', 12], -9.8),
                             (['(', 3, '-', '(', 6,
                                   '*', 4, ')', ')'], -21),
                             ([3, '-', '(', 4, ')', '*', 6], -21),
                             ([3, '-', -1, '*', '(', 4, ')'], 7),
                             (['(', 3, '+', 7, ')', '/',
                               '(', 4, '-', 2, ')'], 5))

    def test_expression_formatter(self):
        '''expressions should be formatted into lists properly'''
        for line, exp_list in self.lines_to_expressions:
            result = runner.expression_form(line)
            self.assertEqual(exp_list, result)

    def test_bad_expressions(self):
        '''expression_form should fail with bad input'''
        for line in self.invalid_lines:
            self.assertRaises(runner.InvalidExpressionError,
                              runner.expression_form, line)

    def test_expression_evaluations(self):
        '''calculator should evaluate expressions correctly'''
        calc = calculator.Calculator()
        for expression, value in self.expressions_to_values:
            result = calc.evaluate(expression)
            self.assertEqual(value, result)

    def test_div_0_fail(self):
        '''calculator should fail to divide by 0'''
        calc = calculator.Calculator()
        self.assertRaises(ZeroDivisionError, calc.evaluate, [12, '/', 0])
                          
if __name__ == '__main__':
    unittest.main()
                             
