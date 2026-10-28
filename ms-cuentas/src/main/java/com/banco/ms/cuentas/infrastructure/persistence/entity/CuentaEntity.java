package com.banco.ms.cuentas.infrastructure.persistence.entity;

import com.banco.ms.cuentas.domain.model.TipoCuenta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cuenta")
@Getter
@Setter
@NoArgsConstructor
public class CuentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", nullable = false, unique = true, length = 20)
    private String numeroCuenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false, length = 20)
    private TipoCuenta tipoCuenta;

    @Column(name = "saldo_inicial", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial;

    @Column(name = "saldo_disponible", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoDisponible;

    @Column(nullable = false)
    private Boolean estado;

    @Column(name = "cliente_id", nullable = false, length = 36)
    private String clienteId;
}
