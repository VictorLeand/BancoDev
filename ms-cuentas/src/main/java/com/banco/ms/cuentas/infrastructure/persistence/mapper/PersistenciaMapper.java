package com.banco.ms.cuentas.infrastructure.persistence.mapper;

import com.banco.ms.cuentas.domain.model.ClienteSnapshot;
import com.banco.ms.cuentas.domain.model.Cuenta;
import com.banco.ms.cuentas.domain.model.Movimiento;
import com.banco.ms.cuentas.infrastructure.persistence.entity.ClienteSnapshotEntity;
import com.banco.ms.cuentas.infrastructure.persistence.entity.CuentaEntity;
import com.banco.ms.cuentas.infrastructure.persistence.entity.MovimientoEntity;

public final class PersistenciaMapper {

    private PersistenciaMapper() {
    }

    public static CuentaEntity toEntity(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity();
        entity.setId(cuenta.getId());
        entity.setNumeroCuenta(cuenta.getNumeroCuenta());
        entity.setTipoCuenta(cuenta.getTipoCuenta());
        entity.setSaldoInicial(cuenta.getSaldoInicial());
        entity.setSaldoDisponible(cuenta.getSaldoDisponible());
        entity.setEstado(cuenta.isEstado());
        entity.setClienteId(cuenta.getClienteId());
        return entity;
    }

    public static Cuenta toDomain(CuentaEntity entity) {
        return Cuenta.reconstruir(
                entity.getId(),
                entity.getNumeroCuenta(),
                entity.getTipoCuenta(),
                entity.getSaldoInicial(),
                entity.getSaldoDisponible(),
                entity.getEstado(),
                entity.getClienteId());
    }

    public static MovimientoEntity toEntity(Movimiento movimiento, CuentaEntity cuenta) {
        MovimientoEntity entity = new MovimientoEntity();
        entity.setId(movimiento.getId());
        entity.setFecha(movimiento.getFecha());
        entity.setTipoMovimiento(movimiento.getTipoMovimiento());
        entity.setValor(movimiento.getValor());
        entity.setSaldo(movimiento.getSaldo());
        entity.setCuenta(cuenta);
        return entity;
    }

    public static Movimiento toDomain(MovimientoEntity entity) {
        return Movimiento.reconstruir(
                entity.getId(),
                entity.getFecha(),
                entity.getTipoMovimiento(),
                entity.getValor(),
                entity.getSaldo(),
                entity.getCuenta().getNumeroCuenta());
    }

    public static ClienteSnapshotEntity toEntity(ClienteSnapshot snapshot) {
        ClienteSnapshotEntity entity = new ClienteSnapshotEntity();
        entity.setClienteId(snapshot.clienteId());
        entity.setNombre(snapshot.nombre());
        entity.setEstado(snapshot.estado());
        entity.setFechaActualizacion(snapshot.fechaActualizacion());
        return entity;
    }

    public static ClienteSnapshot toDomain(ClienteSnapshotEntity entity) {
        return new ClienteSnapshot(entity.getClienteId(), entity.getNombre(), entity.getEstado(),
                entity.getFechaActualizacion());
    }
}

