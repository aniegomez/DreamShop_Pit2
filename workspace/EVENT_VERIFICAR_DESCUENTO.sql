DROP EVENT IF EXISTS EVENT_VERIFICAR_DESCUENTO;
CREATE EVENT EVENT_VERIFICAR_DESCUENTO
  ON SCHEDULE
    EVERY 1 DAY_MINUTE
  DO
    UPDATE DESCUENTO SET IDESTADO = 2
    WHERE CURRENT_DATE > FE_TERMINO;
    
SET GLOBAL event_scheduler = ON;

SHOW EVENTS;