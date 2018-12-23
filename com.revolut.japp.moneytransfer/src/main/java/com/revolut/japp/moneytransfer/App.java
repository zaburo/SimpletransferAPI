package com.revolut.japp.moneytransfer;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.Map;

import com.revolut.japp.moneytransfer.model.Account;
import com.revolut.japp.moneytransfer.model.Transfer;
import com.revolut.japp.moneytransfer.model.TransferStatus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class App extends AbstractVerticle {

    private final Map<Integer, Account> accounts = new LinkedHashMap<>();
    private final Map<Integer, Transfer> transfers = new LinkedHashMap<>();


    public static void main(final String[] args) {
        Launcher.executeCommand("run", App.class.getName());
    }

    @Override
    public void start(Future<Void> temp) {

        initiateAccount();

        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>MoneyTransfer</h1>");
        });

        router.route("/assets/*").handler(StaticHandler.create("assets"));

        router.route().handler(BodyHandler.create());

        router.get("/api/accounts").handler(this::getAllAccounts);
        router.get("/api/accounts/:id").handler(this::getAccount);
        router.post("/api/accounts").handler(this::addAccount);
        router.put("/api/accounts/:id").handler(this::updateAccount);
        router.delete("/api/accounts/:id").handler(this::deleteAccount);

        router.get("/api/transfers").handler(this::getAllTransfers);
        router.get("/api/transfers/:id").handler(this::getTransfer);
        router.post("/api/transfers").handler(this::addTransfer);
        router.put("/api/transfers/:id").handler(this::updateTransfer);

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        4444,
                        result -> {
                            if (result.succeeded()) {
                                temp.complete();
                            } else {
                                temp.fail(result.cause());
                            }
                        }
                );
    }

    private void getAllAccounts(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(accounts.values()));
    }

    private void getAccount(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer userId = Integer.valueOf(id);
            Account account = accounts.get(userId);
            if (account == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(account));
            }
        }
    }

    private void addAccount(RoutingContext routingContext) {
        try {
            final Account account = Json.decodeValue(routingContext.getBodyAsString(),
                    Account.class);
            accounts.put(account.getUserId(), account);
            routingContext.response()
                    .setStatusCode(201)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(account));
        } catch (Exception e) {
            routingContext.response().setStatusCode(400).end();
        }
    }

    private void updateAccount(RoutingContext routingContext) {
        final String userId = routingContext.request().getParam("id");
        JsonObject json = routingContext.getBodyAsJson();
        if (id == null || json == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer user = Integer.valueOf(userId);
            Account account = accounts.get(user);
            if (account == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                boolean updated = false;
                if (json.getString("name") != null && !json.getString("name").isEmpty()) {
                    account.setUserName(json.getString("name"));
                    updated = true;
                }
                if (json.getString("balance") != null && !json.getString("balance").isEmpty() && (new BigDecimal(json.getString("balance"))).compareTo(BigDecimal.ZERO) >= 0) {
                    account.setBalance(new BigDecimal(json.getString("balance")));
                    updated = true;
                }
                if (json.getString("currency") != null && !json.getString("currency").isEmpty()) {
                    try {
                        account.setCurrency(Currency.getInstance(json.getString("currency")));
                        updated = true;
                    } catch (Exception e) {
                        updated = false;
                    }
                }
                if (!updated) {
                    routingContext.response().setStatusCode(400).end();
                } else {
                    routingContext.response()
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(account));
                }
            }
        }
    }

    private void deleteAccount(RoutingContext routingContext) {
        String userId = routingContext.request().getParam("id");
        if (userId == null) {
            routingContext.response().setStatusCode(400).end();
        } else if (accounts.get(Integer.valueOf(userId)) == null) {
            routingContext.response().setStatusCode(404).end();
        } else {
            Integer user = Integer.valueOf(userId);
            accounts.remove(user);
            routingContext.response().setStatusCode(204).end();
        }
    }

    private void getAllTransfers(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(transfers.values()));
    }

    private void getTransfer(RoutingContext routingContext) {
        final String userId = routingContext.request().getParam("id");
        if (userId == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer user = Integer.valueOf(userId);
            Transfer transfer = transfers.get(user);
            if (transfer == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(transfer));
            }
        }
    }

    private void addTransfer(RoutingContext routingContext) {
        try {
            final Transfer transfer = Json.decodeValue(routingContext.getBodyAsString(),
                    Transfer.class);
            transfers.put(transfer.getTransactionId(), transfer);
            routingContext.response()
                    .setStatusCode(201)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(transfer));
        } catch (Exception e) {
            routingContext.response().setStatusCode(400).end();
        }
    }

    private void updateTransfer(RoutingContext routingContext) {
        final String userId = routingContext.request().getParam("id");
        if (userId == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer user = Integer.valueOf(userId);
            Transfer transfer = transfers.get(user);
            if (transfer == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                if (transfer.getStatus() != TransferStatus.PENDING && transfer.getStatus() != TransferStatus.FAILED &&
            		transfer.getAmount().compareTo(BigDecimal.ZERO) > 0 &&
            		accounts.get(transfer.getFromAccountId()).getBalance().compareTo(transfer.getAmount()) >= 0 &&
            		accounts.get(transfer.getFromAccountId()) != null && accounts.get(transfer.getToAccountId()) != null &&
            		
            		accounts.get(transfer.getFromAccountId()).getCurrency().equals(accounts.get(transfer.getToAccountId()).getCurrency()) &&
            		accounts.get(transfer.getFromAccountId()).getCurrency().equals(transfer.getCurrency()) &&
            		accounts.get(transfer.getToAccountId()).getCurrency().equals(transfer.getCurrency())) 
                {
                    accounts.get(transfer.getFromAccountId()).withdraw(transfer.getAmount());
                    accounts.get(transfer.getToAccountId()).deposit(transfer.getAmount());
                    transfer.setStatus(TransferStatus.PENDING);
                } else {
                    transfer.setStatus(TransferStatus.FAILED);
                }
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(transfer));
            }
        }
    }

    private void initiateAccount() {
        Account account1 = new Account("Yuanwen", new BigDecimal("1111"), Currency.getInstance("EUR"));
        accounts.put(account1.getUserId(), account1);
        Account account2 = new Account("Bach", new BigDecimal("234"), Currency.getInstance("EUR"));
        accounts.put(account2.getUserId(), account2);
        Account account3 = new Account("Caesar", new BigDecimal("10000"), Currency.getInstance("GBP"));
        accounts.put(account3.getUserId(), account3);
        Transfer trans1 = new Transfer(0, 1, new BigDecimal("650"), Currency.getInstance("EUR"), "Rent");
        transfers.put(trans1.getTransactionId(), trans1);
        Transfer trans2 = new Transfer(1, 2, new BigDecimal("200"), Currency.getInstance("USD"), "Gift");
        transfers.put(trans2.getTransactionId(), trans2);
        Transfer trans3 = new Transfer(1, 0, new BigDecimal("100"), Currency.getInstance("EUR"), "Shopping");
        transfers.put(trans3.getTransactionId(), trans3);
    }

}