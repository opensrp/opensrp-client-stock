# opensrp-client-stock
OpenSRP Client Stock Library

## Configurability

By placing a file named `app.properties` in your implementation assets folder (See sample app) , one can configure certain aspects of the app

### Configurable Settings

| Configuration                         | Type    | Default | Description                                                            |
| --------------------------------------| ------- | ------- | -----------------------------------------------------------------------|
| `use.only.doses.for.calculation`      | Boolean | false   | Setting this property to true will use `doses` as default for calculations instead of `vials` and will hide all occurances of `vials`. This should be noted that for proper calculation, the `quantity` parameter for each stock should be set to 1 inside `stock_types.json` file,                                                      |