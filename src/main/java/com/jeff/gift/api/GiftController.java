package com.jeff.gift.api;

import javax.validation.Valid;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeff.gift.domain.Funds;
import com.jeff.gift.domain.Gift;
import com.jeff.gift.domain.GiftResponse;
import com.jeff.gift.domain.Receipt;
import com.jeff.gift.domain.Token;
import com.jeff.gift.service.GiftService;

@RestController
@RequestMapping("/gifts")
public class GiftController {
	
	private final TokenModelAssembler tokenModelAssembler;
	private final GiftService giftService;
	
	public GiftController(TokenModelAssembler tokenModelAssembler, GiftService giftService) {
		this.tokenModelAssembler = tokenModelAssembler;
		this.giftService = giftService;
	}

	@PostMapping
	public ResponseEntity<EntityModel<Token>> create(@Valid Funds funds) {
		EntityModel<Token> res = tokenModelAssembler.toModel(giftService.createGifts(funds));
		
		return ResponseEntity
			    .created(res.getRequiredLink(IanaLinkRelations.SELF).toUri())
			    .body(res);
	}
	
	@GetMapping("/{token}")
	public ResponseEntity<GiftResponse> getGifts(@PathVariable("token") String token) {
		Gift gift = giftService.gotcha(token);
		return ResponseEntity.ok().body(GiftResponse.of(gift));
	}
	
	@GetMapping("/{token}/receipt")
	public ResponseEntity<Receipt> getGiftsInfo(@PathVariable("token") String token) {
		Receipt receipt = giftService.getReceipt(token);
		return ResponseEntity.ok().body(receipt);
	}
	
	
}
