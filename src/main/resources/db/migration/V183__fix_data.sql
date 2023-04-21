UPDATE trip SET joined_payment_id = id WHERE joined_payment_id is null and id in (select id from joined_payment);
