package br.integrado.jnpereira.nutrimix.tools;

import br.integrado.jnpereira.nutrimix.controle.FrmMenuFXML;
import br.integrado.jnpereira.nutrimix.dao.Dao;
import br.integrado.jnpereira.nutrimix.modelo.Pessoa;
import br.integrado.jnpereira.nutrimix.modelo.Produto;
import br.integrado.jnpereira.nutrimix.modelo.Usuario;
import br.integrado.jnpereira.nutrimix.modelo.VendaCompra;
import br.integrado.jnpereira.nutrimix.modelo.VendaCompraProduto;
import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Relatorio {

    private static final String dsDiretorio = "C:\\Windows\\Temp\\";

    Dao dao = new Dao();

    public static void main(String[] args) {
        Relatorio relatorio = new Relatorio();
        //relatorio.gerarReciboVenda(5);
        relatorio.gerarRelatorioEstoque();
    }

    private void abrirPDF(String dsCaminho) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File(dsCaminho));
    }

    private String getDtEmissao() {
        String data;
        data = Data.AmericaToBrasil(Data.getAgora());
        data = data.replace("/", "");
        data = data.replace(":", "");
        data = data.replace(" ", "");
        data += ".pdf";
        return data;
    }

    public Chunk getLinha(int distancia) {
        DottedLineSeparator separator = new DottedLineSeparator();
        separator.setGap(1.2f);
        separator.setLineWidth(0.5f);
        Chunk linebreak = new Chunk(separator);
        return linebreak;
    }

    private Font getFont(float nrTam) {
        return new Font(FontFamily.COURIER, nrTam, Font.BOLD);
    }

    public void gerarReciboVenda(int cdMovto) {
        try {
            float larWight = 78;

            VendaCompra venda = new VendaCompra();
            venda.setCdMovto(cdMovto);
            dao.get(venda);

            Document document = new Document();
            Rectangle one = new Rectangle(80, 500);
            document.setPageSize(one);
            document.setMargins(2, 2, 2, 2);

            String dsCaminho = dsDiretorio + "recibo_compra_" + getDtEmissao();
            PdfWriter.getInstance(document, new FileOutputStream(dsCaminho));
            document.open();
            //Cabecalho
            Paragraph cabecalho = new Paragraph("Recibo de Compra", getFont(5));
            cabecalho.setAlignment("center");
            cabecalho.setLeading(5);
            document.add(cabecalho);
            document.add(getLinha(0));

            //Emitente            
            PdfPTable infoEmit = new PdfPTable(1);
            infoEmit.setLockedWidth(true);
            infoEmit.setTotalWidth(larWight);
            infoEmit.getDefaultCell().setBorder(PdfPCell.NO_BORDER); // Aqui eu tiro a borda
            Paragraph headerP = new Paragraph("Nutri Mix Terraço Grill", getFont(3));
            headerP.setAlignment("center");
            PdfPCell header = new PdfPCell(headerP);
            header.setHorizontalAlignment(1);
            //header.setColspan(2);            
            infoEmit.addCell(header);
            infoEmit.addCell(new Paragraph("CNPJ: 32.236.229/0001-16", getFont(3)));
            infoEmit.addCell(new Paragraph("Endereço: Av. Irmãos Pereira, 1930 - Bela Vista, Campo Mourão - PR, 87300-010", getFont(3)));
            infoEmit.addCell(new Paragraph("Email: nutrimix@gmail.com", getFont(3)));
            infoEmit.addCell(new Paragraph("Fone: (44) 3016-4068", getFont(3)));
            infoEmit.addCell(new Paragraph("Data Emissão: " + Data.AmericaToBrasilSemHora(Data.getAgora()), getFont(3)));
            document.add(infoEmit);
            document.add(getLinha(0));

            //Cliente
            PdfPTable infoCliente = new PdfPTable(1);
            infoCliente.setLockedWidth(true);
            infoCliente.setTotalWidth(larWight);
            infoCliente.getDefaultCell().setBorder(PdfPCell.NO_BORDER); // Aqui eu tiro a borda    
            if (venda.getCdPessoa() != null) {
                Pessoa pessoa = new Pessoa();
                pessoa.setCdPessoa(venda.getCdPessoa());
                dao.get(pessoa);
                infoCliente.addCell(new Paragraph("Cliente: " + pessoa.getDsPessoa(), getFont(2.5f)));
                if (pessoa.getTpPessoa().equals("F")) {
                    infoCliente.addCell(new Paragraph("CPF: " + Numero.NumeroToCPF(pessoa.getNrCpfCnpj()), getFont(2.5f)));
                } else {
                    infoCliente.addCell(new Paragraph("CNPJ: " + Numero.NumeroToCNPJ(pessoa.getNrCpfCnpj()), getFont(2.5f)));
                }
            } else {
                infoCliente.addCell(new Paragraph("Cliente: ", getFont(3)));
                infoCliente.addCell(new Paragraph("CPF/CNPJ: ", getFont(3)));
            }
            document.add(infoCliente);
            document.add(getLinha(0));

            //Venda
            PdfPTable infoVenda = new PdfPTable(2);
            infoVenda.getDefaultCell().setBorder(PdfPCell.NO_BORDER); // Aqui eu tiro a borda  
            infoVenda.setLockedWidth(true);
            infoVenda.setTotalWidth(larWight);
            PdfPCell dtVenda = new PdfPCell(new Paragraph("Dt. Venda: " + Data.AmericaToBrasilSemHora(venda.getDtCadastro()), getFont(2.1f)));
            dtVenda.setPadding(1.5f);
            dtVenda.setBorder(PdfPCell.NO_BORDER);
            infoVenda.addCell(new Paragraph("Cód. Venda: " + venda.getCdMovto(), getFont(2.1f)));
            // infoVenda.addCell(new Paragraph("Dt. Venda: " + Data.AmericaToBrasilSemHora(venda.getDtCadastro()), getFont(2.1f)));
            infoVenda.addCell(dtVenda);
            document.add(infoVenda);
            document.add(getLinha(0));

            //Venda Produto
            PdfPTable infoProd = new PdfPTable(3);

            infoProd.getDefaultCell().setBorder(PdfPCell.NO_BORDER); // Aqui eu tiro a borda  
            infoProd.setLockedWidth(true);
            infoProd.setTotalWidth(larWight);
            infoProd.addCell(new Paragraph("Produto", getFont(2.1f)));
            infoProd.addCell(new Paragraph("Preço", getFont(2.1f)));
            infoProd.addCell(new Paragraph("Total", getFont(2.1f)));

            double vlTotalProd = 0;
            ArrayList<Object> prods = dao.getAllWhere(new VendaCompraProduto(), "WHERE $cdMovto$ = " + venda.getCdMovto() + " ORDER BY $cdProduto$ ASC");
            for (Object obj : prods) {
                VendaCompraProduto prodVenda = (VendaCompraProduto) obj;
                Produto prod = new Produto();
                prod.setCdProduto(prodVenda.getCdProduto());
                dao.get(prod);
                infoProd.addCell(new Paragraph(prod.getCdProduto() + ": " + prod.getDsProduto(), getFont(2.1f)));
                infoProd.addCell(new Paragraph(Numero.doubleToReal(prodVenda.getQtUnitario(), 4) + " X " + Numero.doubleToR$(prodVenda.getVlUnitario()), getFont(2.1f)));
                infoProd.addCell(new Paragraph(Numero.doubleToR$(prodVenda.getQtUnitario() * prodVenda.getVlUnitario()), getFont(2.1f)));
                vlTotalProd += prodVenda.getQtUnitario() * prodVenda.getVlUnitario();
            }
            infoProd.addCell(new Paragraph("", getFont(2.5f)));
            infoProd.addCell(new Paragraph("Total: ", getFont(2.5f)));
            infoProd.addCell(new Paragraph(Numero.doubleToR$(vlTotalProd), getFont(2.5f)));
            document.add(infoProd);
            //Fecha e abre o PDF
            document.close();
            abrirPDF(dsCaminho);
        } catch (Exception ex) {
            Alerta.AlertaError("Erro ao gerar recibo!", ex.toString());
        }
    }

    public void gerarRelatorioEstoque() {
        try {
            Document document = new Document();
            document.setPageSize(PageSize.A4);

            String dsCaminho = dsDiretorio + "relatorio_estoque" + getDtEmissao();
            PdfWriter.getInstance(document, new FileOutputStream(dsCaminho));
            document.open();

            //Cabeçalho
            Paragraph dsRelatorio = new Paragraph("Relatório de Estoque", getFont(14));
            dsRelatorio.setAlignment("center");
            dsRelatorio.setLeading(15);
            document.add(dsRelatorio);
            document.add(getLinha(0));
            Usuario usuario = new Usuario();
            usuario.setCdUsuario(FrmMenuFXML.usuarioAtivo);
            dao.get(usuario);
            Paragraph infoRelatorio = new Paragraph("Emitido por: " + usuario.getDsLogin() + " Dt. Emissão: " + Data.AmericaToBrasil(Data.getAgora()), getFont(9));
            infoRelatorio.setLeading(15);
            document.add(infoRelatorio);
            document.add(getLinha(0));
            
            

            PdfPTable infoProduto = new PdfPTable(5);
            infoProduto.getDefaultCell().setBorder(PdfPCell.NO_BORDER); // Aqui eu tiro a borda
            infoProduto.setTotalWidth(new float[]{200, 80, 80, 80, 100});
            infoProduto.setLockedWidth(true);

            PdfPCell dsProduto = new PdfPCell(new Paragraph("Produto", getFont(8)));
            dsProduto.setPadding(3);
            infoProduto.addCell(dsProduto);

            PdfPCell qtEstoqMin = new PdfPCell(new Paragraph("Qt. Estq. Mín", getFont(8)));
            qtEstoqMin.setPadding(3);
            infoProduto.addCell(qtEstoqMin);

            PdfPCell qtEstoq = new PdfPCell(new Paragraph("Qt. Estoque", getFont(8)));
            qtEstoq.setPadding(3);            
            infoProduto.addCell(qtEstoq);

            PdfPCell vlCustoMedio = new PdfPCell(new Paragraph("Vl. Custo Médio", getFont(8)));
            vlCustoMedio.setPadding(3);            
            infoProduto.addCell(vlCustoMedio);
            
            PdfPCell nrCont = new PdfPCell(new Paragraph("Contagem", getFont(8)));
            nrCont.setPadding(3);            
            infoProduto.addCell(nrCont);

            ArrayList<Object> prods = dao.getAllWhere(new Produto(), "ORDER BY $cdProduto$ ASC");
            for (Object obj : prods) {
                Produto prod = (Produto) obj;
                dsProduto = new PdfPCell(new Paragraph(prod.getCdProduto() + ". " + prod.getDsProduto(), getFont(8)));
                dsProduto.setPadding(1.5f);
                dsProduto.setBorder(PdfPCell.NO_BORDER);
                infoProduto.addCell(dsProduto);

                qtEstoqMin = new PdfPCell(new Paragraph(Numero.doubleToReal(prod.getQtEstoqMin(), 2), getFont(8)));
                qtEstoqMin.setPadding(1.5f);
                qtEstoqMin.setBorder(PdfPCell.NO_BORDER);
                qtEstoqMin.setHorizontalAlignment(1);
                infoProduto.addCell(qtEstoqMin);

                qtEstoq = new PdfPCell(new Paragraph(Numero.doubleToReal(prod.getQtEstqAtual(), 2), getFont(8)));
                qtEstoq.setPadding(1.5f);
                qtEstoq.setBorder(PdfPCell.NO_BORDER);
                qtEstoq.setHorizontalAlignment(1);
                infoProduto.addCell(qtEstoq);

                vlCustoMedio = new PdfPCell(new Paragraph(Numero.doubleToR$(prod.getVlCustoMedio()), getFont(8)));
                vlCustoMedio.setPadding(1.5f);
                vlCustoMedio.setBorder(PdfPCell.NO_BORDER);
                vlCustoMedio.setHorizontalAlignment(1);
                infoProduto.addCell(vlCustoMedio);
                
                nrCont = new PdfPCell(new Paragraph("_________________", getFont(8)));
                nrCont.setPadding(1.5f);
                nrCont.setBorder(PdfPCell.NO_BORDER);
                nrCont.setHorizontalAlignment(1);
                infoProduto.addCell(nrCont);

            }

            document.add(infoProduto);

            document.close();
            abrirPDF(dsCaminho);
        } catch (Exception ex) {
            Alerta.AlertaError("Erro ao gerar relatório!", ex.toString());
        }
    }
}
