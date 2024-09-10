
// File: src/main/kotlin/view/component/PaymentMethodDialog.kt
package view.component

import PaymentWebViewDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.Merchandise
import module.ShopModule

@Composable
fun CartMethodDialog(
    onDismissRequest: () -> Unit,
    onCampusCardPay: () -> Unit,
    onQRCodePay: () -> Unit,
    shopModule: ShopModule,
    //items: List<Merchandise>,
) {
    var showWebViewDialog by remember { mutableStateOf(false) }
    var paymentResult by remember { mutableStateOf<String?>(null) }
    var tempItems by remember { mutableStateOf(listOf<Merchandise>()) }
    var balance by remember { mutableStateOf<String?>(null) }
    val shopModule = ShopModule(
        onSearchSuccess = {},
        onBuySuccess = { result ->
            balance = result
        },
        onEnterSuccess = {},
        onAddItemToCartSuccess = {},
        onRemoveItemFromCartSuccesss = {},
        onShopAddToListSuccess = {},
        onGetAllTransactionsSuccess = {},
        onGetTransactionsByCardNumberSuccess = {},
        onViewSuccess = { result ->
            tempItems = emptyList()
            tempItems = result
        },
        //onViewCartComplete = {},
    )

    if (showWebViewDialog) {
        PaymentWebViewDialog(amount = 1.0) { result ->
            paymentResult = result
            showWebViewDialog = false
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "付款方式") },
        text = { Text(text = "请选择一种付款方式进行支付:") },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        shopModule.viewCart{
                            println("size: ${tempItems.size}")
                            onCampusCardPay()
                            for (i in 0 until tempItems.size) {
                                shopModule.shopBuy(tempItems[i].itemUuid, tempItems[i].quantity, tempItems[i].itemname)
                            }
                        }
                    },
                    modifier = Modifier.size(120.dp, 42.dp)
                ) {
                    Text("校园卡支付")
                }
//                Button(
//                    onClick = { showWebViewDialog = true },
//                    modifier = Modifier.size(120.dp, 42.dp)
//                ) {
//                    Text("扫码支付")
//                }
                var price = 0.0
                if (tempItems.isNotEmpty()) {
                    for (i in 0 until tempItems.size) {
                        price = price + tempItems[i].price.toDouble()
                    }
                } else {
                    price = 1.0
                }
                PaymentWebViewDialog(amount = price) { result ->
                    paymentResult = result
                    showWebViewDialog = false
                    println("Payment result: $paymentResult")
                    if (paymentResult == "success") {
                        for (i in 0 until tempItems.size) {
                            shopModule.shopBuy(tempItems[i].itemUuid, tempItems[i].quantity, tempItems[i].itemname)
                        }
                    }
                }
            }
        }
    )

//    paymentResult?.let {
//        if (it == "success") {
//            shopModule.shopQRBuy(item.itemUuid, item.quantity, item.itemname)
//        }
//    }
}