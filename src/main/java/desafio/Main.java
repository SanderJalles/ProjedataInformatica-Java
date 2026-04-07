package desafio;

import desafio.model.Funcionario;
import desafio.model.Pessoa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Funcionario> funcionarios = new ArrayList<>();

        funcionarios.add(new Funcionario("Maria", LocalDate.parse("18/10/2000", dtf), new BigDecimal("2009.44"), "Operador"));
        funcionarios.add(new Funcionario("João", LocalDate.parse("12/05/1990", dtf), new BigDecimal("2284.38"), "Operador"));
        funcionarios.add(new Funcionario("Caio", LocalDate.parse("02/05/1961", dtf), new BigDecimal("9836.14"), "Coordenador"));
        funcionarios.add(new Funcionario("Miguel", LocalDate.parse("14/10/1988", dtf), new BigDecimal("19119.88"), "Diretor"));
        funcionarios.add(new Funcionario("Alice", LocalDate.parse("05/01/1995", dtf), new BigDecimal("2234.68"), "Recepcionista"));
        funcionarios.add(new Funcionario("Heitor", LocalDate.parse("19/11/1999", dtf), new BigDecimal("1582.72"), "Operador"));
        funcionarios.add(new Funcionario("Arthur", LocalDate.parse("31/03/1993", dtf), new BigDecimal("4071.84"), "Contador"));
        funcionarios.add(new Funcionario("Laura", LocalDate.parse("08/07/1994", dtf), new BigDecimal("3017.45"), "Gerente"));
        funcionarios.add(new Funcionario("Heloísa", LocalDate.parse("24/05/2003", dtf), new BigDecimal("1606.85"), "Eletricista"));
        funcionarios.add(new Funcionario("Helena", LocalDate.parse("02/09/1996", dtf), new BigDecimal("2799.93"), "Gerente"));

        funcionarios.removeIf(f -> f.getNome().equalsIgnoreCase("João"));


        System.out.println(String.format("%-15s | %-12s | %-15s | %-15s", "NOME", "NASCIMENTO", "SALÁRIO", "FUNÇÃO"));
        System.out.println("------------------------------------------------------------------");

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

        for (Funcionario f : funcionarios) {
            String dataFormatada = f.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String salarioFormatado = df.format(f.getSalario());

            System.out.println(String.format("%-15s | %-12s | %-15s | %-15s",
                    f.getNome(),
                    dataFormatada,
                    salarioFormatado,
                    f.getFuncao()));
        }

        funcionarios.forEach(f -> {
            BigDecimal aumento = f.getSalario().multiply(new BigDecimal("0.10"));
            f.setSalario(f.getSalario().add(aumento));
        });

        Map<String, List<Funcionario>> funcionariosPorFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));

        System.out.println("\n--- Funcionários Agrupados por Função ---");
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println("Função: " + funcao);
            lista.forEach(f -> System.out.println("  - " + f.getNome()));
        });

        System.out.println("\n--- Aniversariantes de Outubro (10) e Dezembro (12) ---");
        funcionarios.stream()
                .filter(f -> f.getDataNascimento().getMonthValue() == 10 || f.getDataNascimento().getMonthValue() == 12)
                .forEach(f -> System.out.println(f.getNome() + " - " + f.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM"))));


        Funcionario maisVelho = Collections.min(funcionarios, Comparator.comparing(Pessoa::getDataNascimento));
        int idade = Period.between(maisVelho.getDataNascimento(), LocalDate.now()).getYears();
        System.out.println("\n>>> FUNCIONÁRIO COM MAIOR IDADE");
        System.out.println("Nome: " + maisVelho.getNome() + " | Idade: " + idade + " anos");

        System.out.println("\n>>> LISTA POR ORDEM ALFABÉTICA");
        funcionarios.stream()
                .sorted(Comparator.comparing(Pessoa::getNome))
                .forEach(f -> System.out.println(f.getNome()));

        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("\nTOTAL DOS SALÁRIOS: " + df.format(totalSalarios));

        BigDecimal valorSalarioMinimo = new BigDecimal("1212.00");
        System.out.println("\n>>> EQUIVALÊNCIA EM SALÁRIOS MÍNIMOS");
        for (Funcionario f : funcionarios) {
            BigDecimal qtdMinimos = f.getSalario().divide(valorSalarioMinimo, 2, RoundingMode.HALF_UP);
            System.out.println(String.format("%-10s ganha %s salários mínimos.", f.getNome(), qtdMinimos));
        }
    }
}