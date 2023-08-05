package com.ppfurtado.planpnae.domain.services.servicesImpls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ppfurtado.planpnae.domain.dtos.FichaTecnicaRequest;
import com.ppfurtado.planpnae.domain.mappers.FichaTecnicaMapper;
import com.ppfurtado.planpnae.domain.model.ComposicaoAlimento;
import com.ppfurtado.planpnae.domain.model.FichaTecnica;
import com.ppfurtado.planpnae.domain.model.Ingredientes;
import com.ppfurtado.planpnae.domain.repositories.FichaTecnicaRepository;
import com.ppfurtado.planpnae.domain.repositories.IngredientesRepository;
import com.ppfurtado.planpnae.domain.services.FichaTecnicaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FichaTecnicaServiceImpl implements FichaTecnicaService {

    private final FichaTecnicaRepository fichaTecnicaRepository;
    private final IngredientesRepository ingredientesRepository;

    public FichaTecnicaServiceImpl(FichaTecnicaRepository fichaTecnicaRepository, IngredientesRepository ingredientesRepository) {
        this.fichaTecnicaRepository = fichaTecnicaRepository;
        this.ingredientesRepository = ingredientesRepository;
    }


    @Override
    public List<FichaTecnica> findAll() {
        return fichaTecnicaRepository.findAll();
    }

    @Override
    public FichaTecnica findById(Long id) throws Exception {
        return fichaTecnicaRepository.findById(id).orElseThrow( () ->new Exception("Ficha não encontrada"));
    }

    @Override
    public FichaTecnica save(FichaTecnicaRequest request) throws JsonProcessingException {

        List<Ingredientes> ingrediente = ingredientesRepository.findAllById(request.getIngredientes());

        ComposicaoAlimento composicaoAlimento = new ComposicaoAlimento().soma(ingrediente, request.getPerCapitaLiquido());

        FichaTecnica fichaTecnica = FichaTecnicaMapper.INSTANCE.toEntity(request);

        fichaTecnica.setPerCapitaBrutoToString(request.getPerCapitaBruto());
        fichaTecnica.setPerCapitaLiquidoToString(request.getPerCapitaLiquido());
        fichaTecnica.setFatorDeCorrecaoToString(request.getPerCapitaBruto(), request.getPerCapitaLiquido());
        fichaTecnica.setMedidaCaseiraToString(request.getMedidaCaseira());
        fichaTecnica.setCustoUnitarioToString(request.getCustoUnitario());
        fichaTecnica.setFatorDeCoccao(request.getPerCapitaLiquido());
        fichaTecnica.setModoPreparo(request.getModoPreparo());

        fichaTecnica.setIngredientes(ingrediente);

        return fichaTecnicaRepository.save(fichaTecnica);
    }

    @Override
    public FichaTecnica delete(Long id) throws Exception {
        FichaTecnica fichaTecnica = findById(id);

        fichaTecnicaRepository.delete(fichaTecnica);

        return fichaTecnica;
    }
}
