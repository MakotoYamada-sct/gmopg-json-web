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
