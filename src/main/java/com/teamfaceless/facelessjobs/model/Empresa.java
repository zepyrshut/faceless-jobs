package com.teamfaceless.facelessjobs.model;
import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.teamfaceless.facelessjobs.enums.Provincias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "empresa")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Size(max = 100)
    @Column(name = "logo_empresa")
    private String logoEmpresa;
    
    @Size(max = 45)
    @Column(name = "CIF_empresa", nullable = false, unique = true)
    private String cIFempresa;
    
    @NotEmpty
    @Size(max = 45)
    @Column(name = "nombre_empresa" , nullable = false)
    private String nombreEmpresa;
    
    @Size(max = 45)
    @Column(name = "nombre_juridico_empresa" , nullable = false)
    private String nombreJuridicoEmpresa;
    
    @Size(max = 14)
    @Column(name = "telefono_empresa")
    private String telefonoEmpresa;
    
    @Size(max = 45)
    @Column(name = "whatsapp_empresa")
    private String whatsappEmpresa;
    
    @Size(max = 200)
    @Column(name = "direccion_empresa")
    private String direccionEmpresa;
    
    @Column(name = "provincia_empresa_e")
    private Provincias provinciaEmpresaE;
    
    @Size(max = 200)
    @Column(name = "localidad_empresa")
    private String localidadEmpresa;
    
    @Column(name = "empleados_empresa")
    private Integer empleadosEmpresa;
    
    @Singular
    @OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_empresa_oferta",referencedColumnName = "id_empresa")
    private List<OfertaEmpleo> ofertasEmpleos;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credencial_id_credencial", referencedColumnName = "id_credencial")
    private Credencial credencial;
    
    @JoinColumn(name = "provincia_empresa", referencedColumnName = "id_provincia")
    @ManyToOne
    private Provincia provinciaEmpresa;
    
    @JoinColumn(name = "sector_empresa", referencedColumnName = "id_sector_laboral")
    @ManyToOne
    private SectorLaboral sectorEmpresa;
    
    public void addOfertaEmpleo(OfertaEmpleo ofertaEmpleo) {
    	this.ofertasEmpleos.add(ofertaEmpleo);
    }
}
