package com.ecommerce.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.exception.FeedbackException;
import com.ecommerce.exception.LoginException;
import com.ecommerce.exception.OrderException;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.CurrentUserSession;
import com.ecommerce.model.Customer;
import com.ecommerce.model.Feedback;
import com.ecommerce.model.Order;
import com.ecommerce.repository.CurrentUserSessionRepo;
import com.ecommerce.repository.CustomerRepo;
import com.ecommerce.repository.FeedbackRepo;



@Service
public class FeedbackServiceImplementation implements FeedbackService {

	@Autowired
	private FeedbackRepo fDao;

	@Autowired
	private CustomerRepo uRepo;

	@Autowired
	private CustomerService userService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CurrentUserSessionRepo csdao;

	@Override
	public Feedback updateFeedBack(Feedback feedback, String customerKey, Integer feedbackId)
			throws FeedbackException, LoginException, UserException {

		CurrentUserSession loggedInUser = csdao.findByUuid(customerKey);

		if (loggedInUser == null) {
			throw new LoginException("Invalid Key Entered");
		}

		if (loggedInUser.getCustomer() == false) {
			throw new UserException("Unauthorized Access! Only Customer can make changes");
		}

		Optional<Customer> existingUser = uRepo.findById(loggedInUser.getUserId());

		if (existingUser.isPresent()) {

			Customer user = existingUser.get();

			Optional<Feedback> feedbackOptional = fDao.findById(feedback.getFeedbackId());

			if (feedbackOptional.isPresent()) {

				Feedback feedbackUser = feedbackOptional.get();

				if (feedbackUser.getCustomer().getUserLoginId() != user.getUserLoginId())
					throw new UserException("Invalid User key Entered");

				feedback.setCustomer(feedbackUser.getCustomer());
				feedback.setOrder(feedbackUser.getOrder());
				feedback.setFeedbackdate(LocalDate.now());
				feedback.setStatus("Updated");

				Feedback updatedFeedback = fDao.save(feedback);

				return updatedFeedback;
			} else
				throw new FeedbackException("Invalid Feedback details...!");

		} else {
			throw new UserException("User Not Found");
		}

	}

	@Override
	public Feedback viewFeedBack(Integer feedbackId) throws FeedbackException {

		return fDao.findById(feedbackId)
				.orElseThrow(() -> new FeedbackException("No feedback found with given feedbackId :" + feedbackId));
	}

	@Override
	public List<Feedback> viewAllFeedBack() throws FeedbackException {
		List<Feedback> feedbacks = fDao.findAll();
		if (feedbacks.size() == 0) {
			throw new FeedbackException("No feedback submitted...!");
		}
		return feedbacks;
	}

	@Override
	public Feedback deleteFeedBack(String key, Integer feedbackId)
			throws FeedbackException, LoginException, UserException {

		CurrentUserSession loggedInUser = csdao.findByUuid(key);

		if (loggedInUser == null) {
			throw new LoginException("Invalid Key Entered");
		}

		Customer user = userService.findByUserLoginId(loggedInUser.getUserId());

		Optional<Feedback> feedback = fDao.findById(feedbackId);
		if (feedback.isPresent()) {
			Feedback existingfeedback = feedback.get();

			Customer feedbakUser = existingfeedback.getCustomer();

			if (feedbakUser.getUserLoginId() != user.getUserLoginId())
				throw new UserException("Invalid User key Entered");

			existingfeedback.setCustomer(null);
			existingfeedback.setOrder(null);

			fDao.delete(existingfeedback);

			return existingfeedback;
		} else {
			throw new FeedbackException("No feedback found with given feedbackId :" + feedbackId);
		}

	}

	@Override
	public Feedback addFeedBack(Feedback feedback, String key, Integer orderId)
			throws FeedbackException, UserException, OrderException, LoginException {
		CurrentUserSession loggedInUser = csdao.findByUuid(key);

		if (loggedInUser == null) {
			throw new UserException("user not logged in");
		}

		Order order = orderService.viewOrder(orderId, key);

		if (order == null)
			throw new OrderException("No Order found!");

		Customer user = userService.findByUserLoginId(loggedInUser.getUserId());
		feedback.setCustomer(user);
		feedback.setOrder(order);
		feedback.setFeedbackdate(LocalDate.now());
		feedback.setStatus("New");

		Feedback newFeedback = fDao.save(feedback);
		return newFeedback;

	}

}
