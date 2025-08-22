package com.robato.diagnosticos.domain;
public class Paciente {
    private String nome; private String convenio; private Integer idade; private String sexo;
    public Paciente(){}
    public Paciente(String nome,String convenio,Integer idade,String sexo){this.nome=nome;this.convenio=convenio;this.idade=idade;this.sexo=sexo;}
    public String getNome(){return nome;} public void setNome(String nome){this.nome=nome;}
    public String getConvenio(){return convenio;} public void setConvenio(String convenio){this.convenio=convenio;}
    public Integer getIdade(){return idade;} public void setIdade(Integer idade){this.idade=idade;}
    public String getSexo(){return sexo;} public void setSexo(String sexo){this.sexo=sexo;}
}
