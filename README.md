# gmopg-json-web

`gmopg-json-web` は、GMOペイメントゲートウェイ（GMO-PG）のJSON APIを利用した決済処理・Web連携を簡単に実行するためのWebアプリケーションです。

---

## 概要

本Webシステムは、GMO-PGが提供する決済API（クレジットカード決済・マルチ決済など）とのリクエスト/レスポンスのJSON通信を行うWebアプリケーションです。

---

## 主な機能

- **取引・決済管理**: 取引登録（ExecTran / EntryTran）、決済実行、キャンセル・金額変更

---

## インストール

プロジェクトの依存関係に合わせて以下のコマンドでインストールしてください。

```bash
# Node.js / npm の場合
npm install [https://github.com/MakotoYamada-sct/gmopg-json-web.git](https://github.com/MakotoYamada-sct/gmopg-json-web.git)

# または yarn の場合
yarn add [https://github.com/MakotoYamada-sct/gmopg-json-web.git](https://github.com/MakotoYamada-sct/gmopg-json-web.git)
```

application.propertiesの作成
src/main/resources/application.properties
```text
spring.application.name=gmopg-json-web
server.port=8080

# GMO-PG API Configuration (Test Environment)
gmo.api.url=https://pt01.mul-pay.jp/payment
gmo.api.pass=your_api_pass
gmo.site.id=your_site_id
gmo.site.pass=your_site_pass
gmo.shop.id=your_shop_id
gmo.shop.pass=your_shop_pass
gmo.shop.order.prefix=ORDER
gmo.h2.console.path=http://localhost:8080/h2-console

# H2データベースの接続設定（組み込みモード：アプリ起動時に自動で生成・起動）
spring.datasource.url=jdbc:h2:tcp://localhost/~/gmopg-json-web;AUTO_SERVER=TRUE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
# spring.datasource.generate-unique-name=false

# H2 Console（ブラウザからDBの中身を確認するツール）を有効化
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

H2Databaseのインストールとテーブル作成
```SQL:テーブル作成
CREATE TABLE ENTRYTRAN (
  SHOPID       VARCHAR(13)
 ,SHOPPASS     VARCHAR(64)
 ,ORDERID      VARCHAR(27) UNIQUE
 ,JOBCD        VARCHAR(7)
 ,ITEMCODE     VARCHAR(7)
 ,AMOUNT       VARCHAR(7)
 ,TAX          VARCHAR(7)
 ,TDFLAG       VARCHAR(1)
 ,TDTENANTNAME VARCHAR(25)
 ,TDS2TYPE     VARCHAR(1)
 ,TDREQUIRED   VARCHAR(1)
 ,ACCESSID     VARCHAR(32)
 ,ACCESSPASS   VARCHAR(32)
 ,MEMBERID     VARCHAR(60)
 ,STATUS       VARCHAR(11)
 ,PROCESSDATE  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
